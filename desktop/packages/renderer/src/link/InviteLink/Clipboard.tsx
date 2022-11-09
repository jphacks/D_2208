import {
  Button,
  Flex,
  Input,
  InputGroup,
  InputRightElement,
  Text,
  useClipboard,
} from "@chakra-ui/react";
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

      <InputGroup flexGrow="1">
        <Input value={text} isReadOnly />

        <InputRightElement w="9.25em">
          <Button
            h="1.75rem"
            leftIcon={<ClipboardIcon />}
            colorScheme="green"
            onClick={onCopy}
          >
            {hasCopied ? "コピー完了" : "コピーする"}
          </Button>
        </InputRightElement>
      </InputGroup>
    </Flex>
  );
};
