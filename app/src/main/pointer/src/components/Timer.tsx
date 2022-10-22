import {
  Flex,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper,
  Icon,
  VStack,
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  InputGroup,
  InputLeftAddon,
  InputRightAddon,
  Heading,
  useToast,
} from "@chakra-ui/react";
import { Cog6ToothIcon } from "@heroicons/react/24/solid";
import { FC, useEffect, useState } from "react";
import { useForm } from "react-hook-form";

import { stompClient } from "@/stomp";
import { AuthData } from "@/types/AuthData";

type Props = {
  authData: AuthData;
};

type FormValues = {
  minutes: string;
  seconds: string;
};

export const Timer: FC<Props> = ({ authData }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [state, setState] = useState<"READY" | "RUNNING">("READY");
  const [finishTimestamp, setFinishedTimestamp] = useState<number | null>(null);
  const [noticeTime, setNoticeTime] = useState<string>("1");

  const toast = useToast();

  const completeNoticeTime = () => {
    if (noticeTime === "") {
      setNoticeTime("0");
    }
    onClose();
  };

  const { register, handleSubmit, setValue } = useForm<FormValues>();

  const onStartStop = (values: FormValues) => {
    console.log(values);
    switch (state) {
      case "READY": {
        stompClient.publish({
          destination: `/app/rooms/${authData.roomId}/timer/start`,
          body: JSON.stringify({
            value: Number(values.minutes) * 60 + Number(values.seconds),
          }),
        });
        return;
      }
      case "RUNNING": {
        stompClient.publish({
          destination: `/app/rooms/${authData.roomId}/timer/stop`,
        });
        return;
      }
    }
  };

  useEffect(() => {
    stompClient.subscribe(
      `/topic/rooms/${authData.roomId}/timer`,
      (message) => {
        console.log(message);
        const { status, value, finishAt } = JSON.parse(message.body) as {
          status: 0 | 1;
          value: number;
          finishAt: string;
        };
        switch (status) {
          case 0: {
            const minutes = Math.floor(value / 60);
            const seconds = value % 60;
            setValue("minutes", minutes.toString());
            setValue("seconds", seconds.toString());
            setState("READY");
            return;
          }
          case 1: {
            setFinishedTimestamp(
              performance.now() + new Date(finishAt).valueOf() - Date.now()
            );
            setState("RUNNING");
            return;
          }
        }
      }
    );
  }, [authData.roomId, setValue]);

  useEffect(() => {
    if (finishTimestamp === null) {
      return;
    }

    let requestId: number | null;

    const tick = (now: number) => {
      const timeStamp = finishTimestamp - now;

      if (timeStamp <= 0) {
        setValue("minutes", "0");
        setValue("seconds", "0");
        setState("READY");
        toast({
          title: "タイマーが終了しました",
          description: "お疲れ様でした",
          status: "success",
          duration: 9000,
          isClosable: true,
        });
        return;
      }

      const seconds = Math.floor((timeStamp / 1000) % 60);
      const minutes = Math.floor((timeStamp / 1000 / 60) % 60);
      setValue("minutes", minutes.toString());
      setValue("seconds", seconds.toString());

      requestId = requestAnimationFrame(tick);
    };

    if (state === "RUNNING") {
      requestId = requestAnimationFrame(tick);
    }

    return () => {
      if (requestId) {
        cancelAnimationFrame(requestId);
      }
    };
  }, [finishTimestamp, setValue, state]);

  return (
    <VStack gap={2}>
      <Heading fontSize="xl">タイマー</Heading>
      <VStack gap={4} as="form" onSubmit={handleSubmit(onStartStop)}>
        <Flex justify="center" align="center">
          <NumberInput defaultValue={5} min={0} size="lg" flexGrow={1}>
            <NumberInputField
              textAlign="right"
              fontSize="5xl"
              height={24}
              {...register("minutes", {
                required: true,
              })}
            />
            <NumberInputStepper>
              <NumberIncrementStepper />
              <NumberDecrementStepper />
            </NumberInputStepper>
          </NumberInput>
          <Icon
            h={16}
            w={16}
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M10.5 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 12a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z"
              clipRule="evenodd"
            />
          </Icon>
          <NumberInput defaultValue={0} min={0} max={59} size="lg" flexGrow={1}>
            <NumberInputField
              textAlign="right"
              fontSize="5xl"
              height={24}
              {...register("seconds", {
                required: true,
              })}
            />
            <NumberInputStepper>
              <NumberIncrementStepper />
              <NumberDecrementStepper />
            </NumberInputStepper>
          </NumberInput>
        </Flex>
        <Flex gap={8}>
          <Button width={24} type="submit">
            {state === "READY" ? "スタート" : "ストップ"}
          </Button>
          <Button onClick={onOpen}>
            <Icon as={Cog6ToothIcon} />
          </Button>
        </Flex>
      </VStack>
      <Modal isOpen={isOpen} onClose={completeNoticeTime}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>残り時間通知</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <InputGroup>
              <InputLeftAddon>残り</InputLeftAddon>
              <NumberInput
                min={0}
                value={noticeTime}
                onChange={(value) => setNoticeTime(value)}
              >
                <NumberInputField />
                <NumberInputStepper>
                  <NumberIncrementStepper />
                  <NumberDecrementStepper />
                </NumberInputStepper>
              </NumberInput>
              <InputRightAddon>分</InputRightAddon>
            </InputGroup>
          </ModalBody>

          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={completeNoticeTime}>
              完了
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </VStack>
  );
};
