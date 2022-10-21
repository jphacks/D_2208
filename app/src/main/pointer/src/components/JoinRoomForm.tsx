import {
  VStack,
  FormControl,
  FormLabel,
  Input,
  FormHelperText,
  Button,
  FormErrorMessage,
} from "@chakra-ui/react";
import { FC, useState } from "react";
import { useForm } from "react-hook-form";

import { roomApi } from "@/api";
import { AuthData } from "@/types/AuthData";

type Props = {
  onSubmit: (authData: AuthData) => void;
};

const localStorageKey = "lastUserName";

type FormValues = {
  userName: string;
  passcode: string;
  roomId: string;
};

export const JoinRoomForm: FC<Props> = ({ onSubmit: onSubmitProps }) => {
  const url = new URL(location.href);

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
  } = useForm<FormValues>({
    defaultValues: {
      userName: localStorage.getItem(localStorageKey) ?? "",
      passcode: url.searchParams.get("passcode") ?? "",
      roomId: url.searchParams.get("roomId") ?? "",
    },
  });

  const onSubmit = async (values: FormValues) => {
    console.log(values);
    const { data } = await roomApi.joinRoom(values.roomId, {
      passcode: values.passcode,
      name: values.userName,
    });
    localStorage.setItem(localStorageKey, values.userName);
    onSubmitProps({
      ...data,
      userName: values.userName,
    });
  };

  return (
    <VStack py="8" as="form" onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={errors.userName !== undefined}>
        <FormLabel>ユーザ名</FormLabel>
        <Input
          type="text"
          {...register("userName", {
            required: "ユーザ名は必須です",
            maxLength: {
              value: 255,
              message: "255文字以下にしてください",
            },
          })}
        />
        <FormErrorMessage>{errors.userName?.message}</FormErrorMessage>
        <FormHelperText>1 〜 255 文字</FormHelperText>
      </FormControl>
      <FormControl isInvalid={errors.roomId !== undefined}>
        <FormLabel>ルームID</FormLabel>
        <Input
          type="text"
          {...register("roomId", {
            required: "ルームIDは必須です",
          })}
        />
        <FormErrorMessage>{errors.roomId?.message}</FormErrorMessage>
      </FormControl>
      <FormControl isInvalid={errors.passcode !== undefined}>
        <FormLabel>パスコード</FormLabel>
        <Input
          type="text"
          {...register("passcode", {
            required: "パスコードは必須です",
          })}
        />
        <FormErrorMessage>{errors.passcode?.message}</FormErrorMessage>
      </FormControl>
      <Button isLoading={isSubmitting} type="submit">
        ルームに参加
      </Button>
    </VStack>
  );
};
