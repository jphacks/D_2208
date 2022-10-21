import react from "@vitejs/plugin-react";
import { rmSync } from "fs";
import path from "path";
import { defineConfig, normalizePath } from "vite";
import electron from "vite-electron-plugin";
import { alias } from "vite-electron-plugin/plugin";

rmSync(path.resolve(__dirname, "dist-electron"), {
  recursive: true,
  force: true,
});

export default defineConfig(({ mode }) => ({
  server: {
    port: 7777,
  },
  resolve: {
    alias: {
      "@": normalizePath(path.resolve(__dirname, "src")),
    },
  },
  build: {
    rollupOptions: {
      input: {
        link: normalizePath(path.resolve(__dirname, "link.html")),
        // overlay: path.resolve(__dirname, "overlay.html"),
      },
    },
  },
  plugins: [
    react(),
    electron({
      include: ["electron"],
      plugins: [
        alias([
          {
            find: "@",
            replacement: normalizePath(
              path.resolve(__dirname, "dist-electron", "main")
            ),
          },
        ]),
      ],
    }),
  ],
  esbuild:
    mode === "production"
      ? {
          drop: ["console", "debugger"],
        }
      : {},
}));
