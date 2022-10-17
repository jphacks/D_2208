import { useState } from "react";
import { Container } from "@chakra-ui/react";
import { UserNameForm } from "./components/UserNameForm";
import { Paginator } from "./components/Paginator";
import { Timer } from "./components/Timer";
import { Pointer } from "./components/Pointer";
import { Header } from "./components/Header";
import { VStack } from "@chakra-ui/react";

export const App = () => {
  const [userName, setUserName] = useState<string | null>(null);

  return (
    <Container size="md">
      {userName ? (
        <VStack gap={8} align="stretch" py={4}>
          {/* TODO: <WebSocketProvider> */}
          <Header userName={userName} />
          <Timer />
          <Pointer />
          <Paginator />
          {/* </WebSocketProvider> */}
        </VStack>
      ) : (
        <UserNameForm onSubmit={(name) => setUserName(name)} />
      )}
    </Container>
  );
};
