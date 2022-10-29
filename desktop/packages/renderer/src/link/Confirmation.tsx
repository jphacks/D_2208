import { Button, Center, Container } from "@chakra-ui/react";
import type { FC } from "react";

type Props = {
  onConfirm: () => void;
};

export const Confirmation: FC<Props> = ({ onConfirm }) => {
  return (
    <Container h="full">
      <Center h="full">
        <Button onClick={onConfirm} padding="8">
          クリックして招待リンクを表示
        </Button>
      </Center>
    </Container>
  );
};
