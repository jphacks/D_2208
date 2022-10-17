import {
  VStack,
  FormControl,
  FormLabel,
  Input,
  FormHelperText,
  Button,
} from "@chakra-ui/react";
import { FC, useState } from "react";

type Props = {
  onSubmit: (name: string) => void;
};

const localStorageKey = "lastUserName";

export const UserNameForm: FC<Props> = ({ onSubmit }) => {
  const [userName, setUserName] = useState(
    () => localStorage.getItem(localStorageKey) ?? ""
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    localStorage.setItem(localStorageKey, userName);
    onSubmit(userName);
  };

  return (
    <VStack py="8" as="form" onSubmit={handleSubmit}>
      <FormControl>
        <FormLabel>ユーザ名</FormLabel>
        <Input
          type="text"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
          required
        />
        <FormHelperText>1 〜 255 文字</FormHelperText>
      </FormControl>
      <Button type="submit">Submit</Button>
    </VStack>
  );
};
