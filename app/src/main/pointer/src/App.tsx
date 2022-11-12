import { Container, useToast, VStack } from "@chakra-ui/react";
import { useEffect, useState } from "react";

import { graphql } from "@/gql";

import { closeWsClient, requestWs } from "./api";
import { JoinRoomForm } from "./components/JoinRoomForm";
import { Paginator } from "./components/Paginator";
import { Pointer } from "./components/Pointer";
import { Timer } from "./components/Timer";
import { AuthData } from "./types/AuthData";

export const App = () => {
  const toast = useToast();
  const [authData, setAuthData] = useState<AuthData | null>(null);

  useEffect(() => {
    if (!authData) {
      return;
    }

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToRoomFinishEvent($roomId: ID!) {
            subscribeToRoomFinishEvent(roomId: $roomId)
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          if (data?.subscribeToRoomFinishEvent) {
            closeWsClient();
            setAuthData(null);
            toast({
              title: "ルームが終了しました",
              status: "info",
              isClosable: true,
            });
          }

          if (errors) {
            for (const error of errors) {
              toast({
                title: "エラーが発生しました",
                description: error.message,
                status: "error",
                duration: 3000,
                isClosable: true,
              });
            }
          }
        },
        error: (error) => {
          if (error instanceof Error) {
            toast({
              title: "エラーが発生しました",
              description: error.message,
              status: "error",
              duration: 3000,
              isClosable: true,
            });
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  }, [authData, toast]);

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
