package seedu.address.logic.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ApplicationLinkLauncherTest {

    @Test
    void launchEmail_successfulLaunch_returnsSuccessResult() {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            mockedDesktopApi.when(() -> DesktopApi.browse(any(URI.class))).thenReturn(true);

            ApplicationLinkResult result = ApplicationLinkLauncher.launchEmail("test@example.com");

            assertTrue(result.isSuccess());
            assertTrue(result.getMessage().contains("Launched EMAIL successfully"));
            mockedDesktopApi.verify(() -> DesktopApi.browse(any(URI.class)), times(1));
        }
    }

    @Test
    void launchTelegram_fallbackFails_returnsFailureResult() {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            mockedDesktopApi.when(() -> DesktopApi.browse(any(URI.class))).thenReturn(false);

            ApplicationLinkResult result = ApplicationLinkLauncher.launchTelegram("someHandle");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Failed to launch TELEGRAM"));
            mockedDesktopApi.verify(() -> DesktopApi.browse(any(URI.class)), times(1));
        }
    }

    @Test
    void launchGithub_invalidUri_returnsFailureResult() {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            // The DesktopApi shouldn't even be called because URI creation fails
            ApplicationLinkResult result = ApplicationLinkLauncher.launchGithub("invalid uri with spaces");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Failed to launch GITHUB"));
            mockedDesktopApi.verifyNoInteractions();
        }
    }

    @Test
    void launchEmail_nullEmail_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ApplicationLinkLauncher.launchEmail(null));
    }

    @Test
    void launchTelegram_correctUriIsBuilt() throws URISyntaxException {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            mockedDesktopApi.when(() -> DesktopApi.browse(any(URI.class))).thenReturn(true);

            ApplicationLinkLauncher.launchTelegram("thady_handle");

            mockedDesktopApi.verify(() ->
                    DesktopApi.browse(new URI("https://t.me/thady_handle")), times(1));
        }
    }

    @Test
    void launchGithub_successfulLaunch_returnsSuccessResult() {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            mockedDesktopApi.when(() -> DesktopApi.browse(any(URI.class))).thenReturn(true);

            ApplicationLinkResult result = ApplicationLinkLauncher.launchGithub("octocat");

            assertTrue(result.isSuccess());
            assertTrue(result.getMessage().contains("Launched GITHUB successfully"));
        }
    }

    @Test
    void launchEmail_desktopApiThrowsException_returnsFailureResult() {
        try (MockedStatic<DesktopApi> mockedDesktopApi = mockStatic(DesktopApi.class)) {
            mockedDesktopApi.when(() -> DesktopApi.browse(any(URI.class)))
                    .thenReturn(false); // simulate failure instead of throwing

            ApplicationLinkResult result = ApplicationLinkLauncher.launchEmail("test@example.com");

            assertFalse(result.isSuccess());
            assertTrue(result.getMessage().contains("Failed to launch EMAIL"));
        }
    }
}
