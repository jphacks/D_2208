import {
  Icon,
  Flex,
  VStack,
  Button,
  useDisclosure,
  IconProps,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalCloseButton,
  Center,
  IconButton,
  HStack,
  Spacer,
  Image,
  ChakraProps,
  useToken,
  useToast,
} from "@chakra-ui/react";
import { ArrowPathIcon } from "@heroicons/react/24/solid";
import { FC, useEffect, useState } from "react";

import { requestWs } from "@/api";
import {
  requestPermission,
  subscribeOrientation,
  unsubscribeOrientation,
} from "@/deviceorientation";
import { graphql } from "@/gql";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export type BuiltInPointerType = {
  id: "SPOTLIGHT" | "ARROW" | "FINGER";
  label: string;
};

export type CustomPointerType = {
  id: string;
  label: string;
  url?: string;
};

export type PointerType = BuiltInPointerType | CustomPointerType;

export const builtInPointers: PointerType[] = [
  {
    id: "SPOTLIGHT",
    label: "スポットライト",
  },
  {
    id: "ARROW",
    label: "矢印",
  },
  {
    id: "FINGER",
    label: "人差し指",
  },
];

/**
 *
 * @see https://gist.github.com/0x263b/2bdd90886c2036a1ad5bcf06d6e6fb37
 */
function randomColorFromList(str: string, list: string[]): string {
  let index = 0;
  if (str.length === 0) return list[0]!;
  for (let i = 0; i < str.length; i += 1) {
    index = str.charCodeAt(i) + ((index << 5) - index);
    index = index & index;
  }
  index = ((index % list.length) + list.length) % list.length;
  return list[index]!;
}

const SpotLightIcon: FC<IconProps> = (props) => (
  <Icon xmlns="http://www.w3.org/2000/svg" viewBox="0 0 25 25" {...props}>
    <circle cx="12.5" cy="12.5" r="12" fill="currentColor" />
  </Icon>
);

const ArrowIcon: FC<IconProps> = (props) => (
  <Icon viewBox="0 0 44 58" xmlns="http://www.w3.org/2000/svg" {...props}>
    <path
      d="M1 53.7579V4.24287C1 1.57012 4.23149 0.231635 6.12137 2.12159L41.127 37.1287C43.0168 39.0186 41.6783 42.25 39.0056 42.25H20.9946C20.199 42.25 19.4359 42.566 18.8733 43.1286L6.1212 55.8793C4.23126 57.7691 1 56.4305 1 53.7579Z"
      fill="currentColor"
      stroke="white"
      strokeWidth="2"
    />
  </Icon>
);

const FingerIcon: FC<IconProps> = (props) => (
  <Icon viewBox="0 0 128 128" xmlns="http://www.w3.org/2000/svg" {...props}>
    <path
      fill="currentcolor"
      d="M120.1,95.1c-0.2,0-0.3,0.1-0.5,0.2c-0.2,0.1-23.4,11.3-39.5,11.3 h-1.9c-20.1,0-25-0.9-26.2-1.5c-4.2-2-4.6-6.4-4.2-8c0.1-0.5,0.2-1,0.2-1.4l5.8-0.7c0.6-0.1,0.6-1.9,0-2L47,92.3 c-0.6-1-1.4-1.9-2.5-2.7c-0.9-0.7-2.2-2-2.9-3.5c-0.8-2-1-3.8-0.5-5.4c0.2-0.5,0.3-1.1,0.3-1.6c0.2,0.1,0.3,0.1,0.5,0.1l12.2-0.8 c1.2-0.1,1.2-1.8,0-2L41,75.7c0,0-0.1,0-0.1,0c-0.4-1.1-1.1-2.1-2-2.9c-2.9-2.5-3.8-5.9-2.5-10.1c0-0.1,0.1-0.3,0.1-0.4l17.2-0.7 c1.8-0.1,1.8-2.1,0-2.2l-19.4-0.4c-0.2-0.1-0.5-0.1-0.7-0.1h0l-19.6,0c-1.4,0-6.8-1.3-6.8-7.3c0-5.1,6.4-5.3,6.5-5.3l57-0.1h0 c0.1,0,0.2,0,0.2,0c0.2,0,0.4-0.1,0.6-0.1l7-0.1c1.4,0,1.2-2.1,0.1-2.7l-6.4-2.6c-0.3-0.2-0.7-0.4-1.1-0.4l-1.9-0.7l0,0.1 C68.1,39,66.1,38,63,36.2c-8.4-4.9-9.2-8.3-7.8-12.3c0.8-2.2,3.4-2.6,4.9-2.6c1.8,0,3,0.5,3.4,0.7c5.5,3.7,10.8,5.8,16.4,8.1 c5.8,2.3,11.7,4.8,18.4,9.1c1.1,0.7,2.1,1.6,3,2.7c4.6,5.8,10.5,9.7,17.2,11.3l1.3,0.3c0.5,0.1,0.8,0.5,0.8,1l0.3,39.7 C120.9,94.6,120.6,95,120.1,95.1z"
    />
  </Icon>
);

const PointerIcon: FC<{ pointer: PointerType } & ChakraProps> = ({
  pointer,
  ...props
}) => {
  switch (pointer.id) {
    case "SPOTLIGHT": {
      return <SpotLightIcon {...props} />;
    }
    case "ARROW": {
      return <ArrowIcon transform="translateX(15%)" {...props} />;
    }
    case "FINGER": {
      return <FingerIcon {...props} />;
    }
    default: {
      return <Image src={(pointer as CustomPointerType).url} {...props} />;
    }
  }
};

export const Pointer: FC<Props> = ({ authData }) => {
  const toast = useToast();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [isActive, setIsActive] = useState<boolean>(false);

  const [pointerType, setPointerType] = useState<PointerType["id"] | null>(
    null
  );

  const [customPointers, setCustomPointers] = useState<
    CustomPointerType[] | null
  >(null);

  const colors = useToken("colors", [
    "gray.500",
    "red.500",
    "orange.500",
    "yellow.500",
    "green.500",
    "teal.500",
    "blue.500",
    "cyan.500",
    "purple.500",
    "pink.500",
  ]);

  const color = randomColorFromList(authData.userId, colors);

  useEffect(() => {
    if (!isActive) {
      return;
    }

    requestPermission().then(() => {
      subscribeOrientation(authData.accessToken);
    });
    return () => unsubscribeOrientation(authData.accessToken);
  }, [authData.accessToken, isActive]);

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          query GetPointerType($roomId: ID!) {
            getPointerType(roomId: $roomId)
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          if (data) {
            setPointerType(data.getPointerType);
          }
          if (errors) {
            for (const error of errors) {
              toast({
                title: "エラーが発生しました",
                description: error.message,
                status: "error",
              });
            }
          }
        },
        error: (err) => {
          if (err instanceof Error) {
            toast({
              title: "エラーが発生しました",
              description: err.message,
              status: "error",
              duration: 9000,
              isClosable: true,
            });
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  }, [authData.roomId, toast]);

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToPointerType($roomId: ID!) {
            subscribeToPointerType(roomId: $roomId)
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          if (data) {
            setPointerType(data.subscribeToPointerType);
          }
          if (errors) {
            for (const error of errors) {
              toast({
                title: "エラーが発生しました",
                description: error.message,
                status: "error",
              });
            }
          }
        },
        error: (err) => {
          if (err instanceof Error) {
            toast({
              title: "エラーが発生しました",
              description: err.message,
              status: "error",
              duration: 9000,
              isClosable: true,
            });
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  }, [authData.roomId, toast]);

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          query GetCustomPointers($roomId: ID!) {
            getCustomPointers(roomId: $roomId) {
              customPointers {
                id
                label
                url
              }
            }
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          if (data) {
            setCustomPointers(data.getCustomPointers.customPointers);
          }
          if (errors) {
            for (const error of errors) {
              toast({
                title: "エラーが発生しました",
                description: error.message,
                status: "error",
              });
            }
          }
        },
        error: (err) => {
          if (err instanceof Error) {
            toast({
              title: "エラーが発生しました",
              description: err.message,
              status: "error",
            });
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  }, [authData.roomId, toast]);

  useEffect(() => {
    requestWs(
      {
        query: graphql(/* GraphQL */ `
          subscription SubscribeToCustomPointers($roomId: ID!) {
            subscribeToCustomPointers(roomId: $roomId) {
              customPointers {
                id
                label
                url
              }
            }
          }
        `),
        variables: {
          roomId: authData.roomId,
        },
      },
      {
        next: ({ data, errors }) => {
          if (data) {
            setCustomPointers(data.subscribeToCustomPointers.customPointers);
          }
          if (errors) {
            for (const error of errors) {
              toast({
                title: "エラーが発生しました",
                description: error.message,
                status: "error",
              });
            }
          }
        },
        error: (err) => {
          if (err instanceof Error) {
            toast({
              title: "エラーが発生しました",
              description: err.message,
              status: "error",
            });
          }
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        complete: () => {},
      }
    );
  }, [authData.roomId, toast]);

  return (
    <HStack justify="center" spacing="4">
      <Spacer />
      <Button
        disabled={pointerType === null || customPointers === null}
        aria-label={"ポインターを起動"}
        width="full"
        boxSize={40}
        borderRadius="full"
        borderColor="currentcolor"
        onMouseDown={() => setIsActive(true)}
        onMouseUp={() => setIsActive(false)}
        onTouchStart={() => setIsActive(true)}
        onTouchEnd={() => setIsActive(false)}
        color={color}
        p="4"
      >
        <Center h="full" w="full">
          {pointerType && customPointers && (
            <PointerIcon
              pointer={
                builtInPointers
                  .concat(customPointers)
                  .find((pointer) => pointer.id === pointerType)!
              }
              h="80%"
              w="auto"
            />
          )}
        </Center>
      </Button>
      <Spacer>
        <Flex h="full" align="end">
          <IconButton
            disabled={pointerType === null || customPointers === null}
            icon={<Icon as={ArrowPathIcon} />}
            aria-label="ポインターを切り替える"
            borderRadius="full"
            onClick={onOpen}
          />
        </Flex>
      </Spacer>
      {pointerType && customPointers && (
        <Modal isOpen={isOpen} onClose={onClose} motionPreset="slideInBottom">
          <ModalOverlay />
          <ModalContent mt="auto" mb="0" borderBottomRadius="0">
            <ModalHeader>ポインター切り替え</ModalHeader>
            <ModalCloseButton />
            <ModalBody overflow="hidden">
              <VStack align="stretch" spacing={1} overflow="auto" h="80">
                {builtInPointers.concat(customPointers).map((pointer) => (
                  <Button
                    key={pointer.id}
                    aria-label={pointer.label}
                    width="full"
                    h="16"
                    flexShrink={0}
                    onClick={() => {
                      onClose();
                      requestWs(
                        {
                          query: graphql(/* GraphQL */ `
                            mutation ChangePointerType(
                              $roomId: ID!
                              $pointerType: String!
                            ) {
                              changePointerType(
                                roomId: $roomId
                                pointerType: $pointerType
                              )
                            }
                          `),
                          variables: {
                            roomId: authData.roomId,
                            pointerType: pointer.id,
                          },
                        },
                        {
                          next: ({ errors }) => {
                            if (errors) {
                              for (const error of errors) {
                                toast({
                                  title: "エラーが発生しました",
                                  description: error.message,
                                  status: "error",
                                });
                              }
                            }
                          },
                          error: (err) => {
                            if (err instanceof Error) {
                              toast({
                                title: "エラーが発生しました",
                                description: err.message,
                                status: "error",
                                duration: 9000,
                                isClosable: true,
                              });
                            }
                          },
                          // eslint-disable-next-line @typescript-eslint/no-empty-function
                          complete: () => {},
                        }
                      );
                    }}
                  >
                    <Flex justify="start" gap="4" w="full" align="center">
                      <PointerIcon pointer={pointer} boxSize="6" />
                      {pointer.label}
                    </Flex>
                  </Button>
                ))}
              </VStack>
            </ModalBody>
          </ModalContent>
        </Modal>
      )}
    </HStack>
  );
};
