import { Container , VStack } from "@chakra-ui/react";
import { useState } from "react";

import { Header } from "./components/Header";
import { Paginator } from "./components/Paginator";
import { Pointer } from "./components/Pointer";
import { Timer } from "./components/Timer";
import { UserNameForm } from "./components/UserNameForm";


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
