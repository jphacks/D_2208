import react from "@vitejs/plugin-react";
import { join } from "node:path";
import { renderer } from "unplugin-auto-expose";
import { defineConfig } from "vite";

import { chrome } from "../../.electron-vendors.cache.json";

const PACKAGE_ROOT = __dirname;

export default defineConfig({
  mode: process.env["MODE"]!,
  root: PACKAGE_ROOT,
  resolve: {
    alias: {
      "@/": join(PACKAGE_ROOT, "src") + "/",
    },
  },
  base: "",
  server: {
    fs: {
      strict: true,
    },
  },
  build: {
    sourcemap: true,
    target: `chrome${chrome}`,
    outDir: "dist",
    assetsDir: ".",
    rollupOptions: {
      input: {
        link: join(PACKAGE_ROOT, "link.html"),
        overlay: join(PACKAGE_ROOT, "overlay.html"),
        customPointerTypes: join(PACKAGE_ROOT, "customPointerTypes.html"),
      },
    },
    emptyOutDir: true,
    reportCompressedSize: false,
  },
  plugins: [
    react(),
    renderer.vite({
      preloadEntry: join(PACKAGE_ROOT, "../preload/src/index.ts"),
    }),
  ],
  esbuild:
    process.env["MODE"] === "production"
      ? {
          drop: ["console", "debugger"],
        }
      : {},
});
