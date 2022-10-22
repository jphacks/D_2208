import { Container, VStack } from "@chakra-ui/react";
import { useState } from "react";

import { Header } from "./components/Header";
import { JoinRoomForm } from "./components/JoinRoomForm";
import { Paginator } from "./components/Paginator";
import { Pointer } from "./components/Pointer";
import { Timer } from "./components/Timer";
import { AuthData } from "./types/AuthData";

export const App = () => {
  const [authData, setAuthData] = useState<AuthData | null>(null);

  return (
    <Container size="md" w="full" h="full">
      {authData ? (
        <VStack gap={6} align="stretch" py={4} h="full">
          {/*<Header authData={authData} />*/}
          <Timer authData={authData} />
          <Pointer authData={authData} />
          <Paginator authData={authData} />
        </VStack>
      ) : (
        <JoinRoomForm onSubmit={setAuthData} />
      )}
    </Container>
  );
};
