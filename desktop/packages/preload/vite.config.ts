import { preload } from "unplugin-auto-expose";
import { defineConfig } from "vite";

import { chrome } from "../../.electron-vendors.cache.json";

const PACKAGE_ROOT = __dirname;

export default defineConfig({
  mode: process.env["MODE"]!,
  root: PACKAGE_ROOT,
  envDir: process.cwd(),
  build: {
    ssr: true,
    sourcemap: "inline",
    target: `chrome${chrome}`,
    outDir: "dist",
    assetsDir: ".",
    minify: process.env["MODE"] !== "development",
    lib: {
      entry: "src/index.ts",
      formats: ["cjs"],
    },
    rollupOptions: {
      output: {
        entryFileNames: "[name].cjs",
      },
    },
    emptyOutDir: true,
    reportCompressedSize: false,
  },
  plugins: [preload.vite()],
});
