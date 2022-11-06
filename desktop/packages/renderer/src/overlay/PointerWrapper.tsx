import { Box, Flex, Text, useToken } from "@chakra-ui/react";
import { useWindowSize } from "@react-hook/window-size";
import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import type { FC, ReactNode } from "react";

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

type Props = {
  pointers: UpdatePointersMessage;
  renderPointer: (color: string) => ReactNode;
};

export const PointerWrapper: FC<Props> = ({ pointers, renderPointer }) => {
  const [width, height] = useWindowSize();
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

  return (
    <Box w="full" h="full">
      {pointers.map(({ user, coordinate }) => {
        const color = randomColorFromList(user.id, colors);

        return (
          <Flex
            key={user.id}
            position="absolute"
            top="0"
            left="0"
            fontSize="lg"
            w="fit-content"
            h="fit-content"
            align="end"
            transform={`translate(
              ${width / 2 + coordinate.x * width}px,
              ${height / 2 + coordinate.y * height}px
            )`}
          >
            {renderPointer(color)}
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
