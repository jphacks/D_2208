import react from "@vitejs/plugin-react";
import { rmSync } from "fs";
import path from "path";
import { defineConfig, normalizePath } from "vite";
import electron from "vite-electron-plugin";
import renderer from "vite-plugin-electron-renderer";

rmSync(path.join(__dirname, "dist-electron"), {
  recursive: true,
  force: true,
});

export default defineConfig(({ mode }) => ({
  server: {
    port: 7777,
  },
  resolve: {
    alias: {
      "@": normalizePath(path.join(__dirname, "src")),
    },
  },
  build: {
    rollupOptions: {
      input: {
        link: normalizePath(path.join(__dirname, "link.html")),
        // overlay: path.join(__dirname, "overlay.html"),
      },
    },
  },
  plugins: [
    react(),
    electron({
      include: ["electron", "preload"],
      plugins: [],
    }),
    renderer({
      nodeIntegration: true,
    }),
  ],
  esbuild:
    mode === "production"
      ? {
          drop: ["console", "debugger"],
        }
      : {},
}));
