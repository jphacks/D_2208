import { useState } from "react";
import { Container } from "@chakra-ui/react";
import { UserNameForm } from "./components/UserNameForm";
import { Paginator } from "./components/Paginator";
import { Timer } from "./components/Timer";
import { Pointer } from "./components/Pointer";
import { Header } from "./components/Header";

export const App = () => {
  const [userName, setUserName] = useState<string | null>(null);

  return (
    <Container size="md">
      {userName ? (
        <>
          {/* TODO: <WebSocketProvider> */}
          <Header userName={userName} />
          <Timer />
          <Pointer />
          <Paginator />
          {/* </WebSocketProvider> */}
        </>
      ) : (
        <UserNameForm onSubmit={(name) => setUserName(name)} />
      )}
    </Container>
  );
};
