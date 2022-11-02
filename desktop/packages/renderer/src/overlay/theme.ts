import { extendTheme } from "@chakra-ui/react";

export const theme = extendTheme({
  styles: {
    global: {
      "html, body, #root": {
        w: "100vw",
        h: "100vh",
        overflow: "hidden",
      },
      body: {
        bg: "transparent",
        color: "white",
      },
    },
  },
});
