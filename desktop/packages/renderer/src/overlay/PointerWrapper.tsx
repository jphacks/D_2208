import { Box, Flex, Text } from "@chakra-ui/react";
import { useWindowSize } from "@react-hook/window-size";
import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import type { FC, ReactNode } from "react";

import { useUserColor } from "@/util/useUserColor";

type Props = {
  pointers: UpdatePointersMessage;
  renderPointer: (color: string) => ReactNode;
};

export const PointerWrapper: FC<Props> = ({ pointers, renderPointer }) => {
  const [width, height] = useWindowSize();

  const getUserColor = useUserColor();

  return (
    <Box w="full" h="full">
      {pointers.map(({ user, coordinate }) => {
        const color = getUserColor(user.id);

        return (
          <Flex
            key={user.id}
            position="absolute"
            top="0"
            left="0"
            fontSize={`${Math.min(width, height) / 30}px`}
            w="fit-content"
            h="fit-content"
            align="end"
            transform={`translate(
              ${width / 2 + coordinate.x * width}px,
              ${height / 2 + coordinate.y * height}px
            )`}
          >
            <Flex
              boxSize={`${Math.min(width, height) / 10}px`}
              align="stretch"
              justify="stretch"
            >
              {renderPointer(color)}
            </Flex>
            {pointers.length > 1 && (
              <Box paddingStart="8">
                <Text bg={color} color="white" px="2" rounded="sm">
                  {user.name}
                </Text>
              </Box>
            )}
          </Flex>
        );
      })}
    </Box>
  );
};
