import { IconButton, Icon, Flex, Box, Heading, VStack } from "@chakra-ui/react";
import { FC } from "react";

import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Pointer: FC<Props> = ({ authData }) => {
  return (
    <VStack align="stretch">
      <Heading fontSize="xl" textAlign="center">
        ポインター
      </Heading>
      <Flex justify="stretch">
        <IconButton
          aria-label={"ポインターを起動"}
          width="full"
          height={36}
          variant="link"
          sx={{
            ":active circle": {
              stroke: "green.500",
              fill: "green.500",
            },
          }}
          icon={
            <Icon
              h={32}
              w={32}
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 25 25"
            >
              <Box
                as="circle"
                cx="12.5"
                cy="12.5"
                r="12"
                strokeWidth="0.5"
                stroke="green.300"
                fill="none"
              />
            </Icon>
          }
        />
      </Flex>
    </VStack>
  );
};
