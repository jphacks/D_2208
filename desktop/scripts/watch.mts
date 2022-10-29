#!/usr/bin/env node

import electronPath from "electron";
import { spawn, ChildProcess } from "node:child_process";
import { build, createServer, LogLevel, ViteDevServer } from "vite";

const mode = (process.env["MODE"] = process.env["MODE"] || "development");

const logLevel: LogLevel = "warn";

/**
 * Setup watcher for `main` package
 * On file changed it totally re-launch electron app.
 * Needs to set up `VITE_DEV_SERVER_URL` environment variable from {@link import('vite').ViteDevServer.resolvedUrls}
 */
function setupMainPackageWatcher(rendererWatchServer: ViteDevServer) {
  const viteDevServerUrl = rendererWatchServer.resolvedUrls?.local[0];

  if (viteDevServerUrl === undefined) {
    throw new Error("Vite dev server url is undefined");
  }

  process.env["VITE_DEV_SERVER_URL"] = viteDevServerUrl;

  let electronApp: ChildProcess | null = null;

  return build({
    mode,
    logLevel,
    configFile: "packages/main/vite.config.ts",
    build: {
      /**
       * Set to {} to enable rollup watcher
       * @see https://vitejs.dev/config/build-options.html#build-watch
       */
      watch: {},
    },
    plugins: [
      {
        name: "reload-app-on-main-package-change",
        writeBundle() {
          /** Kill electron if process already exist */
          if (electronApp !== null) {
            electronApp.removeListener("exit", process.exit);
            electronApp.kill("SIGINT");
            electronApp = null;
          }

          /** Spawn new electron process */
          electronApp = spawn(String(electronPath), ["."], {
            stdio: "inherit",
          });

          /** Stops the watch script when the application has been quit */
          electronApp.addListener("exit", process.exit);
        },
      },
    ],
  });
}

/**
 * Setup watcher for `preload` package
 * On file changed it reload web page.
 * Required to access the web socket of the page. By sending the `full-reload` command to the socket, it reloads the web page.
 */
function setupPreloadPackageWatcher(rendererWatchServer: ViteDevServer) {
  return build({
    mode,
    logLevel,
    configFile: "packages/preload/vite.config.ts",
    build: {
      /**
       * Set to {} to enable rollup watcher
       * @see https://vitejs.dev/config/build-options.html#build-watch
       */
      watch: {},
    },
    plugins: [
      {
        name: "reload-page-on-preload-package-change",
        writeBundle() {
          rendererWatchServer.ws.send({
            type: "full-reload",
          });
        },
      },
    ],
  });
}

/**
 * Dev server for Renderer package
 * This must be the first,
 * because the {@link setupMainPackageWatcher} and {@link setupPreloadPackageWatcher}
 * depend on the dev server properties
 */
const rendererWatchServer = await createServer({
  mode,
  logLevel,
  configFile: "packages/renderer/vite.config.ts",
}).then((s) => s.listen());

await setupPreloadPackageWatcher(rendererWatchServer);
await setupMainPackageWatcher(rendererWatchServer);
