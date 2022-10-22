import { Flex, Text } from "@chakra-ui/react";
import { FC } from "react";

import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

export const Header: FC<Props> = ({ authData }) => {
  return (
    <Flex justify="right">
      <Text fontSize="xl">{authData.userName}</Text>
    </Flex>
  );
};
