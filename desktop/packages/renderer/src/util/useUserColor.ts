import { useToken } from "@chakra-ui/react";
import type { User } from "@smartpointer-desktop/shared";
import { useCallback } from "react";

/**
 *
 * @see https://gist.github.com/0x263b/2bdd90886c2036a1ad5bcf06d6e6fb37
 */
export function randomColorFromList(str: string, list: string[]): string {
  let index = 0;
  if (str.length === 0) return list[0]!;
  for (let i = 0; i < str.length; i += 1) {
    index = str.charCodeAt(i) + ((index << 5) - index);
    index = index & index;
  }
  index = ((index % list.length) + list.length) % list.length;
  return list[index]!;
}

export const useUserColor = (): ((userId: User["id"]) => string) => {
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

  const getUserColor = useCallback(
    (userId: User["id"]) => randomColorFromList(userId, colors),
    [colors]
  );

  return getUserColor;
};
