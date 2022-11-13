import { extendTheme } from "@chakra-ui/react";

const styles = {
  global: {
    "html, body, #root": {
      w: "100vw",
      h: "100vh",
    },
  },
};

export const theme = extendTheme({
  styles,
});
