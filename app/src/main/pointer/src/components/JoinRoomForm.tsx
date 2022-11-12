import {
  VStack,
  FormControl,
  FormLabel,
  Input,
  FormHelperText,
  Button,
  FormErrorMessage,
  useToast,
  PinInput,
  PinInputField,
  Flex,
} from "@chakra-ui/react";
import { ClientError } from "graphql-request";
import { FC } from "react";
import { Controller, useForm } from "react-hook-form";

import { initializeWsClient, requestHttp } from "@/api";
import { graphql } from "@/gql";
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

  const toast = useToast();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    control,
  } = useForm<FormValues>({
    defaultValues: {
      userName: localStorage.getItem(localStorageKey) ?? "",
      passcode: url.searchParams.get("passcode") ?? "",
      roomId: url.searchParams.get("roomId") ?? "",
    },
  });

  const onSubmit = async (values: FormValues) => {
    try {
      const data = await requestHttp({
        query: graphql(/* GraphQL */ `
          mutation JoinRoom(
            $roomId: ID!
            $passcode: String!
            $userName: String!
          ) {
            joinRoom(
              roomId: $roomId
              passcode: $passcode
              userName: $userName
            ) {
              tokenType
              accessToken
              ttl
              user {
                id
              }
            }
          }
        `),
        variables: values,
      });

      initializeWsClient(data.joinRoom.accessToken);

      localStorage.setItem(localStorageKey, values.userName);
      onSubmitProps({
        accessToken: data.joinRoom.accessToken,
        userId: data.joinRoom.user.id,
        userName: values.userName,
        roomId: values.roomId,
      });

      toast({
        title: "ログインに成功しました。",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    } catch (error) {
      if (error instanceof ClientError) {
        toast({
          title: "ログインに失敗しました。",
          description: error.response.errors?.map((e) => e.message).join(", "),
          status: "error",
          duration: 3000,
          isClosable: true,
        });
      }
    }
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
        <Flex justify="center" gap="4">
          <Controller
            name="passcode"
            control={control}
            render={({ field: { onChange, value } }) => (
              <PinInput type="number" onChange={onChange} value={value}>
                <PinInputField />
                <PinInputField />
                <PinInputField />
                <PinInputField />
                <PinInputField />
                <PinInputField />
              </PinInput>
            )}
          />
        </Flex>
        <FormErrorMessage>{errors.passcode?.message}</FormErrorMessage>
      </FormControl>
      <Button isLoading={isSubmitting} type="submit">
        ルームに参加
      </Button>
    </VStack>
  );
};
