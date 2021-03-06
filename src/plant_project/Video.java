// http://stackoverflow.com/questions/11006910/encode-a-video-into-h264-using-bufferedimages

package plant_project;

import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.containers.mp4.Brand;
import org.jcodec.containers.mp4.MP4Packet;
import org.jcodec.containers.mp4.TrackType;
import org.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import org.jcodec.containers.mp4.muxer.MP4Muxer;
import org.jcodec.scale.AWTUtil;
import org.jcodec.scale.RgbToYuv420;

public class Video {
	private SeekableByteChannel ch;
	private Picture toEncode;
	private RgbToYuv420 transform;
	private H264Encoder encoder;
	private ArrayList<ByteBuffer> spsList;
	private ArrayList<ByteBuffer> ppsList;
	private FramesMP4MuxerTrack outTrack;
	private ByteBuffer _out;
	private int frameNo;
	private MP4Muxer muxer;
	private int fps;

	public Video(File out, int fps) {
		try {
			this.ch = NIOUtils.writableFileChannel(out);
			this.fps = fps;

			// Transform to convert between RGB and YUV
			transform = new RgbToYuv420(0, 0);

			// Muxer that will store the encoded frames
			muxer = new MP4Muxer(ch, Brand.MP4);

			// Add video track to muxer
			outTrack = muxer.addTrackForCompressed(TrackType.VIDEO, fps);

			// Allocate a buffer big enough to hold output frames
			_out = ByteBuffer.allocate(1920 * 1080 * 6);

			// Create an instance of encoder
			encoder = new H264Encoder();

			// Encoder extra data ( SPS, PPS ) to be stored in a special place of
			// MP4
			spsList = new ArrayList<ByteBuffer>();
			ppsList = new ArrayList<ByteBuffer>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void encodeImage(BufferedImage bi) {
		try {
			if (toEncode == null) {
				toEncode = Picture.create(bi.getWidth(), bi.getHeight(), ColorSpace.YUV420);
			}

			// Perform conversion
			transform.transform(AWTUtil.fromBufferedImage(bi), toEncode);

			// Encode image into H.264 frame, the result is stored in '_out' buffer
			_out.clear();
			ByteBuffer result = encoder.encodeFrame(_out, toEncode);

			// Based on the frame above form correct MP4 packet
			spsList.clear();
			ppsList.clear();
			H264Utils.encodeMOVPacket(result, spsList, ppsList);

			// Add packet to video track
			outTrack.addFrame(new MP4Packet(result, frameNo, fps, 1, frameNo, true, null, frameNo, 0));

			frameNo++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void finish() {
		try {
			// Push saved SPS/PPS to a special storage in MP4
			outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList, ppsList));

			// Write MP4 header and finalize recording
			muxer.writeHeader();
			NIOUtils.closeQuietly(ch);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
