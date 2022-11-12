import { ChakraProvider } from "@chakra-ui/react";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { App } from "./App";
import { theme } from "./theme";

// HTML で定義されているので disable
// eslint-disable-next-line @typescript-eslint/no-non-null-assertion
createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ChakraProvider theme={theme}>
      <App />
    </ChakraProvider>
  </StrictMode>
);
