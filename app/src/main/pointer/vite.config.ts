import basicSsl from "@vitejs/plugin-basic-ssl";
import react from "@vitejs/plugin-react";
import path from "path";
import { defineConfig } from "vitest/config";

export default defineConfig({
  plugins: [react(), basicSsl()],
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
      "/graphql": {
        target: "http://localhost:8080",
      },
      "/ws": {
        target: "ws://localhost:8080",
        ws: true,
      },
      "/graphql-ws": {
        target: "ws://localhost:8080",
        ws: true,
      },
    },
  },
  test: {},
});
