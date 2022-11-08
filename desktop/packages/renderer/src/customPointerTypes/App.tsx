import {
  TabList,
  TabPanel,
  TabPanels,
  Tab,
  Tabs,
  VStack,
  Image,
  Flex,
  Button,
  Heading,
  Icon,
  Text,
  Spacer,
  HStack,
} from "@chakra-ui/react";
import type { CustomPointerType } from "@smartpointer-desktop/shared";
import { FC, useEffect, useState } from "react";

import { CustomPointerTypeTabItem } from "./CustomPointerTypeTabItem";

import {
  getCustomPointerTypes,
  onUpdateCustomPointerTypes,
  addCustomPointerType,
} from "#preload";

export const App: FC = () => {
  const [customPointerTypes, setCustomPointerTypes] = useState<
    CustomPointerType[]
  >([]);

  useEffect(() => {
    getCustomPointerTypes().then(setCustomPointerTypes);
    onUpdateCustomPointerTypes(setCustomPointerTypes);
  }, []);

  return (
    <VStack w="full" h="full" align="stretch" spacing="0">
      <Flex
        align="center"
        py="2"
        px="4"
        borderBottom="solid"
        borderBottomColor="gray.200"
        borderBottomWidth="thin"
      >
        <Heading fontSize="xl">カスタムポインターの設定</Heading>
        <Spacer />
        <Button colorScheme="green" onClick={addCustomPointerType}>
          追加
        </Button>
      </Flex>
      <Tabs
        orientation="vertical"
        variant="unstyled"
        isLazy
        isManual
        flexGrow="1"
        overflow="hidden"
      >
        <TabList
          w="60"
          h="full"
          overflowY="scroll"
          flexShrink={0}
          sx={{
            "&::-webkit-scrollbar": {
              width: "0.5rem",
              backgroundColor: "gray.200",
            },
            "&::-webkit-scrollbar-thumb": {
              backgroundColor: "gray.500",
              borderRadius: "0.25rem",
            },
          }}
        >
          {customPointerTypes.map(({ id, name, image }) => (
            <Tab
              key={id}
              justifyContent="flex-start"
              rounded="0"
              translate="yes"
              _selected={{
                bg: "gray.100",
                color: "gray.900",
                fontWeight: "semibold",
                borderLeft: "solid",
                borderLeftWidth: "thick",
                borderLeftColor: "green.500",
              }}
              _hover={{
                bg: "gray.100",
              }}
              _focusVisible={{
                isolation: "isolate",
                boxShadow: "outline",
              }}
            >
              <HStack py="2" spacing="3">
                {image ? (
                  <Image src={image} boxSize="8" />
                ) : (
                  <Icon
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="gray.600"
                    boxSize="8"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636"
                    />
                  </Icon>
                )}
                <Text
                  textAlign="left"
                  color={name === "" ? "gray.500" : "currentColor"}
                  flexGrow={1}
                >
                  {name || "名前が設定されていません"}
                </Text>
              </HStack>
            </Tab>
          ))}
        </TabList>
        <TabPanels>
          {customPointerTypes.map((customPointerType) => (
            <TabPanel key={customPointerType.id}>
              <CustomPointerTypeTabItem customPointerType={customPointerType} />
            </TabPanel>
          ))}
        </TabPanels>
      </Tabs>
    </VStack>
  );
};
