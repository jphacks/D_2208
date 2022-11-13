import { app, shell } from "electron";
import { URL } from "url";

const localFileOrigin =
  import.meta.env.DEV && import.meta.env.VITE_DEV_SERVER_URL
    ? new URL(import.meta.env.VITE_DEV_SERVER_URL).origin
    : "file://";

const allowedExternalOrigins = new Set(["https://smartpointer.abelab.dev"]);

app.on("web-contents-created", (_, contents) => {
  contents.on("will-navigate", (event, url) => {
    const { origin } = new URL(url);
    if (origin !== localFileOrigin) {
      event.preventDefault();
      console.warn(`Blocked navigating to disallowed origin: ${origin}`);
    }
  });

  contents.session.setPermissionRequestHandler(
    (webContents, permission, callback) => {
      callback(false);
      if (import.meta.env.DEV) {
        const { origin } = new URL(webContents.getURL());
        console.warn(
          `${origin} requested permission for '${permission}', but was rejected.`
        );
      }
    }
  );

  contents.setWindowOpenHandler(({ url }) => {
    const { origin } = new URL(url);

    if (allowedExternalOrigins.has(origin)) {
      shell.openExternal(url).catch(console.error);
    } else if (import.meta.env.DEV) {
      console.warn(`Blocked the opening of a disallowed origin: ${origin}`);
    }

    return { action: "deny" };
  });

  contents.on("will-attach-webview", (event, webPreferences, params) => {
    if (!params["src"]) {
      event.preventDefault();
      return;
    }

    const { origin } = new URL(params["src"]);
    if (localFileOrigin !== origin) {
      if (import.meta.env.DEV) {
        console.warn(
          `A webview tried to attach ${params["src"]}, but was blocked.`
        );
      }

      event.preventDefault();
      return;
    }

    delete webPreferences.preload;
    // @ts-expect-error `preloadURL` exists. - @see https://www.electronjs.org/docs/latest/api/web-contents#event-will-attach-webview
    delete webPreferences.preloadURL;

    webPreferences.nodeIntegration = false;
    webPreferences.contextIsolation = true;
  });
});
