import { Box, Text, useToken, VStack } from "@chakra-ui/react";
import { useWindowSize } from "@react-hook/window-size";
import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import type { FC } from "react";

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

export const ArrowPointer: FC<{ pointers: UpdatePointersMessage }> = ({
  pointers,
}) => {
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
          <VStack
            key={user.id}
            position="absolute"
            top="0"
            left="0"
            fontSize="xl"
            w="fit-content"
            h="fit-content"
            align="start"
            gap="0"
            transform={`translate(
              ${width / 2 + coordinate.x * width}px,
              ${height / 2 + coordinate.y * height}px
            )`}
          >
            <svg
              width="44"
              height="58"
              viewBox="0 0 44 58"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              style={{
                width: "auto",
                height: "3em",
              }}
            >
              <path
                d="M1 53.7579V4.24287C1 1.57012 4.23149 0.231635 6.12137 2.12159L41.127 37.1287C43.0168 39.0186 41.6783 42.25 39.0056 42.25H20.9946C20.199 42.25 19.4359 42.566 18.8733 43.1286L6.1212 55.8793C4.23126 57.7691 1 56.4305 1 53.7579Z"
                fill={color}
                stroke="white"
                strokeWidth="2"
              />
            </svg>
            {pointers.length > 1 && (
              <Box paddingStart="8">
                <Text bg={color} color="white" px="4" rounded="md">
                  {user.name}
                </Text>
              </Box>
            )}
          </VStack>
        );
      })}
    </Box>
  );
};
