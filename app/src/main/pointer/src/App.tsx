import { useState } from "react";
import { Container } from "@chakra-ui/react";
import { UserNameForm } from "./components/UserNameForm";

export const App = () => {
  const [userName, setUserName] = useState<string | null>(null);

  return (
    <Container size="md">
      {userName ? (
        <>
          {/* TODO: ↓消す */}
          <h1>{userName}</h1>
          {/* TODO: <WebSocketProvider> */}
          {/* <Timer /> */}
          {/* <Pointer /> */}
          {/* <Paginator /> */}
          {/* </WebSocketProvider> */}
        </>
      ) : (
        <UserNameForm onSubmit={(name) => setUserName(name)} />
      )}
    </Container>
  );
};
