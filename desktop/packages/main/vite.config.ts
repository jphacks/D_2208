import { join } from "node:path";
import { defineConfig } from "vite";

import { node } from "../../.electron-vendors.cache.json";

const PACKAGE_ROOT = __dirname;

export default defineConfig({
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion -- MODE is set by scripts/watch
  mode: process.env["MODE"]!,
  root: PACKAGE_ROOT,
  envDir: process.cwd(),
  resolve: {
    alias: {
      "@/": join(PACKAGE_ROOT, "src") + "/",
    },
  },
  build: {
    ssr: true,
    sourcemap: "inline",
    target: `node${node}`,
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
});
