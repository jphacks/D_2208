import react from "@vitejs/plugin-react";
import path from "path";
import { defineConfig } from "vitest/config";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.join(__dirname, "src"),
    },
  },
  server: {
    port: 8888,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
      },
      "/ws": {
        target: "ws://localhost:8080",
        ws: true,
      },
    },
  },
  test: {},
});
