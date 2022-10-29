import { Button, Flex, Input, Text, useClipboard } from "@chakra-ui/react";
import type { FC } from "react";

import { ClipboardIcon } from "./ClipboardIcon";

type Props = {
  title: string;
  text: string;
};

export const Clipboard: FC<Props> = ({ title, text }) => {
  const { hasCopied, onCopy } = useClipboard(text);

  return (
    <Flex gap="4">
      <Text fontSize="md" width="32" my="auto">
        {title}
      </Text>

      <Input value={text} isReadOnly />

      <Button
        leftIcon={<ClipboardIcon />}
        colorScheme="green"
        onClick={onCopy}
        width="48"
      >
        {hasCopied ? "コピーした" : "コピー"}
      </Button>
    </Flex>
  );
};
