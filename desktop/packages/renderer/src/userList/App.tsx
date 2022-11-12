import { HStack, Icon, StackDivider, VStack } from "@chakra-ui/react";
import type { User } from "@smartpointer-desktop/shared";
import { FC, useEffect, useState } from "react";

import { useUserColor } from "@/util/useUserColor";

export const App: FC = () => {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    document.title = `参加者一覧 (${users.length})`;
  }, [users.length]);

  useEffect(() => {
    // TODO: query users from main process
  }, []);

  useEffect(() => {
    // TODO: subscribe users from main process
  }, []);

  const getUserColor = useUserColor();

  return (
    <VStack
      p="4"
      spacing="4"
      divider={<StackDivider borderColor="gray.200" />}
      align="start"
    >
      {users.map((user) => (
        <HStack key={user.id}>
          <Icon viewBox="0 0 16 16" color={getUserColor(user.id)} boxSize="6">
            <circle fill="currentColor" cx="8" cy="8" r="8" />
          </Icon>
          <p>{user.name}</p>
        </HStack>
      ))}
    </VStack>
  );
};
