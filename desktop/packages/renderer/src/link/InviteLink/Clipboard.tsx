import {
  Button,
  Flex,
  Icon,
  Input,
  InputGroup,
  InputRightElement,
  Text,
  useClipboard,
} from "@chakra-ui/react";
import { ClipboardIcon as ClipboardIconOutline } from "@heroicons/react/24/outline";
import { ClipboardIcon as ClipboardIconSolid } from "@heroicons/react/24/solid";
import type { FC } from "react";

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
            color="white"
            h="1.75rem"
            leftIcon={
              <Icon>
                {hasCopied ? <ClipboardIconSolid /> : <ClipboardIconOutline />}
              </Icon>
            }
            // leftIcon={<ClipboardIcon />}
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
