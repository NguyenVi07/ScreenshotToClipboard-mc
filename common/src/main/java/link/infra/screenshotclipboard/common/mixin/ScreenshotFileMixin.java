package link.infra.screenshotclipboard.common.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import link.infra.screenshotclipboard.common.MacOSCompat;
import link.infra.screenshotclipboard.common.ScreenshotToClipboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(NativeImage.class)
public abstract class ScreenshotFileMixin {
	@Inject(method = "writeToFile(Ljava/io/File;)V", at = @At("HEAD"))
	private void screenshotclipboard$copyBeforeScreenshotSave(File file, CallbackInfo ci) {
		if (isScreenshotFile(file) && !ScreenshotToClipboard.isMacOS()) {
			ScreenshotToClipboard.handleScreenshotAWT((NativeImage) (Object) this);
		}
	}

	@Inject(method = "writeToFile(Ljava/io/File;)V", at = @At("TAIL"))
	private void screenshotclipboard$copyMacScreenshotAfterSave(File file, CallbackInfo ci) {
		if (isScreenshotFile(file) && ScreenshotToClipboard.isMacOS()) {
			MacOSCompat.doCopyMacOS(file.getAbsolutePath());
		}
	}

	private static boolean isScreenshotFile(File file) {
		File parent = file.getParentFile();
		return parent != null && "screenshots".equals(parent.getName());
	}
}
