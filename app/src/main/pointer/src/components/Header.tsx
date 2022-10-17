import { FC } from "react";
import { Flex, Text } from "@chakra-ui/react";

type Props = {
  userName: string;
};

export const Header: FC<Props> = ({ userName }) => {
  return (
    <Flex justify="right">
      <Text fontSize="xl">{userName}</Text>
    </Flex>
  );
};