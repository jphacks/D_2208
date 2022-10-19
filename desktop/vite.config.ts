import react from "@vitejs/plugin-react";
import { rmSync } from "fs";
import path from "path";
import { defineConfig } from "vite";
import electron from "vite-electron-plugin";
import { alias } from "vite-electron-plugin/plugin";

rmSync(path.join(__dirname, "dist-electron"), { recursive: true, force: true });

export default defineConfig({
  resolve: {
    alias: {
      "@": path.join(__dirname, "src"),
      styles: path.join(__dirname, "src/assets/styles"),
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
            replacement: path.join(__dirname, "main"),
          },
        ]),
      ],
    }),
  ],
  clearScreen: false,
});
