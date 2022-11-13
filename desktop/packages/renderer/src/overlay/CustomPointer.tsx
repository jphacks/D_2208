import { Box } from "@chakra-ui/react";
import type {
  CustomPointerType,
  UpdatePointersMessage,
} from "@smartpointer-desktop/shared";
import type { FC } from "react";

import { PointerWrapper } from "./PointerWrapper";

export const CustomPointer: FC<{
  pointers: UpdatePointersMessage;
  pointerType: CustomPointerType;
}> = ({ pointers, pointerType }) => (
  <PointerWrapper
    pointers={pointers}
    renderPointer={() => (
      <Box
        backgroundImage={`url(${pointerType.content})`}
        backgroundRepeat="no-repeat"
        backgroundSize="contain"
        boxSize="full"
      />
    )}
  />
);
