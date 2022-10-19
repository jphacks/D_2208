import { rmSync } from "fs";
import path from "path";
import { defineConfig } from "vite";
import electron from "vite-electron-plugin";
import { alias } from "vite-electron-plugin/plugin";

rmSync(path.join(__dirname, "dist-electron"), { recursive: true, force: true });

console.log(path.resolve(__dirname, "electron", "main"));
export default defineConfig({
  resolve: {
    alias: {
      "@": path.join(__dirname, "src"),
    },
  },
  plugins: [
    electron({
      include: ["electron"],
      plugins: [
        alias([
          {
            find: "@",
            replacement: path.resolve(__dirname, "dist-electron", "main"),
          },
        ]),
      ],
    }),
  ],
});
