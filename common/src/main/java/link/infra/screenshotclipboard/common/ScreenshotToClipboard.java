package link.infra.screenshotclipboard.common;

import com.mojang.blaze3d.platform.NativeImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.util.Locale;

public final class ScreenshotToClipboard {
	public static final String MOD_ID = "screenshotclipboard";
	private static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotToClipboard");

	private ScreenshotToClipboard() {}

	public static boolean isMacOS() {
		return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("mac");
	}

	public static void init() {
		if (!isMacOS()) {
			try {
				Toolkit.getDefaultToolkit().getSystemClipboard();
			} catch (HeadlessException e) {
				LOGGER.warn("java.awt.headless was not disabled early enough", e);
			}
		}
	}

	public static void handleScreenshotAWT(NativeImage image) {
		if (isMacOS()) {
			return;
		}

		if (image.format() != NativeImage.Format.RGBA) {
			LOGGER.warn("Could not copy screenshot: unsupported image format {}", image.format());
			return;
		}

		ByteBuffer pixels = image.getPixelBytes();
		byte[] copy = new byte[pixels.remaining()];
		pixels.get(copy);
		doCopy(copy, image.getWidth(), image.getHeight(), 4);
	}

	public static void handleScreenshotAWT(ByteBuffer byteBuffer, int width, int height, int components) {
		if (isMacOS()) {
			return;
		}

		ByteBuffer pixels = byteBuffer.duplicate();
		byte[] copy = new byte[pixels.remaining()];
		pixels.get(copy);
		doCopy(copy, width, height, components);
	}

	private static void doCopy(byte[] imageData, int width, int height, int components) {
		new Thread(() -> {
			DataBufferByte buffer = new DataBufferByte(imageData, imageData.length);
			ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
			int[] componentBits = {8, 8, 8};
			int[] bandOffsets = {0, 1, 2};
			ComponentColorModel colorModel = new ComponentColorModel(colorSpace, componentBits, false, false,
					Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
			BufferedImage bufferedImage = new BufferedImage(colorModel, Raster.createInterleavedRaster(buffer,
					width, height, width * components, components, bandOffsets, null), false, null);

			Transferable transferable = getTransferableImage(bufferedImage);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(transferable, null);
		}, "Screenshot to Clipboard Copy").start();
	}

	private static Transferable getTransferableImage(BufferedImage bufferedImage) {
		return new Transferable() {
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[]{DataFlavor.imageFlavor};
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
				if (DataFlavor.imageFlavor.equals(flavor)) {
					return bufferedImage;
				}
				throw new UnsupportedFlavorException(flavor);
			}
		};
	}
}
