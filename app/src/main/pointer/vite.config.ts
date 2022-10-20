import react from "@vitejs/plugin-react";
import { defineConfig } from "vitest/config";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: "http://smartpointer.abelab.dev",
        changeOrigin: true,
      }
    }
  },
  test: {},
});
