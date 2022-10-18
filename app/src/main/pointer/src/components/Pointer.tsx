import { IconButton, Icon, Flex } from "@chakra-ui/react";
import { FC } from "react";

export const Pointer: FC = () => {
  return (
    <Flex justify="center">
      <IconButton
        aria-label={"ポインターを起動"}
        width="full"
        height={36}
        variant="link"
        color="green.300"
        _active={{
          color: "green.500",
        }}
        icon={
          <Icon
            h={32}
            w={32}
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <circle cx="12" cy="12" r="12" />
          </Icon>
        }
      />
    </Flex>
  );
};
