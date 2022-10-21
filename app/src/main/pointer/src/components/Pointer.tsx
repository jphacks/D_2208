import { IconButton, Icon, Flex, Box, Heading, VStack } from "@chakra-ui/react";
import { FC, useEffect, useState } from "react";

import {
  requestPermission,
  subscribeOrientation,
  unsubscribeOrientation,
} from "@/deviceorientation";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Pointer: FC<Props> = ({ authData }) => {
  const [isActive, setIsActive] = useState<boolean>(false);

  useEffect(() => {
    if (!isActive) {
      return;
    }

    requestPermission().then(() => {
      subscribeOrientation(authData.roomId);
    });
    return () => unsubscribeOrientation(authData.roomId);
  }, [authData.roomId, isActive]);

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
          onMouseDown={() => setIsActive(true)}
          onMouseUp={() => setIsActive(false)}
          onTouchStart={() => setIsActive(true)}
          onTouchEnd={() => setIsActive(false)}
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
                stroke={isActive ? "green.500" : "green.300"}
                fill={isActive ? "green.500" : "none"}
              />
            </Icon>
          }
        />
      </Flex>
    </VStack>
  );
};
