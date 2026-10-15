package link.infra.screenshotclipboard;

import link.infra.screenshotclipboard.common.ScreenshotToClipboard;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = ScreenshotToClipboard.MOD_ID, dist = Dist.CLIENT)
public class ScreenshotToClipboardForgeClient {
	public ScreenshotToClipboardForgeClient(IEventBus modBus) {
		ScreenshotToClipboard.init();
	}
}
