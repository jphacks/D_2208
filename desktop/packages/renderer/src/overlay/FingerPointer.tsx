import { Box, Flex, Text, useToken } from "@chakra-ui/react";
import { useWindowSize } from "@react-hook/window-size";
import type { Pointers } from "@smartpointer-desktop/shared";
import type { FC } from "react";

/**
 *
 * @see https://gist.github.com/0x263b/2bdd90886c2036a1ad5bcf06d6e6fb37
 */
function randomColorFromList(str: string, list: string[]): string {
  let index = 0;
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  if (str.length === 0) return list[0]!;
  for (let i = 0; i < str.length; i += 1) {
    index = str.charCodeAt(i) + ((index << 5) - index);
    index = index & index;
  }
  index = ((index % list.length) + list.length) % list.length;
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  return list[index]!;
}

export const FingerPointer: FC<{ pointers: Pointers }> = ({ pointers }) => {
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
    <Box w="full" h="full" overflow="hidden">
      {pointers.map(({ pointer, userId, name }) => {
        const color = randomColorFromList(userId, colors);

        return (
          <Flex
            key={userId}
            position="absolute"
            top="0"
            left="0"
            fontSize="xl"
            w="fit-content"
            h="fit-content"
            align="center"
            gap="4"
            transform={`translate(
              ${width / 2 + pointer.x * width}px,
              ${height / 2 + pointer.y * height}px
            )`}
          >
            <svg
              viewBox="0 0 128 128"
              width="128"
              height="128"
              style={{
                width: "auto",
                height: "3em",
              }}
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fill={color}
                d="M120.1,95.1c-0.2,0-0.3,0.1-0.5,0.2c-0.2,0.1-23.4,11.3-39.5,11.3 h-1.9c-20.1,0-25-0.9-26.2-1.5c-4.2-2-4.6-6.4-4.2-8c0.1-0.5,0.2-1,0.2-1.4l5.8-0.7c0.6-0.1,0.6-1.9,0-2L47,92.3 c-0.6-1-1.4-1.9-2.5-2.7c-0.9-0.7-2.2-2-2.9-3.5c-0.8-2-1-3.8-0.5-5.4c0.2-0.5,0.3-1.1,0.3-1.6c0.2,0.1,0.3,0.1,0.5,0.1l12.2-0.8 c1.2-0.1,1.2-1.8,0-2L41,75.7c0,0-0.1,0-0.1,0c-0.4-1.1-1.1-2.1-2-2.9c-2.9-2.5-3.8-5.9-2.5-10.1c0-0.1,0.1-0.3,0.1-0.4l17.2-0.7 c1.8-0.1,1.8-2.1,0-2.2l-19.4-0.4c-0.2-0.1-0.5-0.1-0.7-0.1h0l-19.6,0c-1.4,0-6.8-1.3-6.8-7.3c0-5.1,6.4-5.3,6.5-5.3l57-0.1h0 c0.1,0,0.2,0,0.2,0c0.2,0,0.4-0.1,0.6-0.1l7-0.1c1.4,0,1.2-2.1,0.1-2.7l-6.4-2.6c-0.3-0.2-0.7-0.4-1.1-0.4l-1.9-0.7l0,0.1 C68.1,39,66.1,38,63,36.2c-8.4-4.9-9.2-8.3-7.8-12.3c0.8-2.2,3.4-2.6,4.9-2.6c1.8,0,3,0.5,3.4,0.7c5.5,3.7,10.8,5.8,16.4,8.1 c5.8,2.3,11.7,4.8,18.4,9.1c1.1,0.7,2.1,1.6,3,2.7c4.6,5.8,10.5,9.7,17.2,11.3l1.3,0.3c0.5,0.1,0.8,0.5,0.8,1l0.3,39.7 C120.9,94.6,120.6,95,120.1,95.1z"
              />
            </svg>
            {pointers.length > 1 && (
              <Text bg={color} color="white" px="4" rounded="md">
                {name}
              </Text>
            )}
          </Flex>
        );
      })}
    </Box>
  );
};
