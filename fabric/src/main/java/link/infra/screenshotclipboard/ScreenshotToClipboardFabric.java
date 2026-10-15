package link.infra.screenshotclipboard;

import link.infra.screenshotclipboard.common.ScreenshotToClipboard;
import net.fabricmc.api.ClientModInitializer;

public class ScreenshotToClipboardFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenshotToClipboard.init();
	}
}
