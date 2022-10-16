import { FC, useState, useCallback } from "react";
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
} from "@chakra-ui/react";
import { Cog6ToothIcon } from "@heroicons/react/24/solid";

export const Timer: FC = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [state, setState] = useState<"READY" | "RUNNING">("READY");
  const [noticeTime, setNoticeTime] = useState<string>("1");

  const completeNoticeTime = () => {
    if (noticeTime === "") {
      setNoticeTime("0");
    }
    onClose();
  };

  return (
    <VStack>
      <Flex justify="center" align="center">
        <NumberInput defaultValue={5} min={0} size="lg" maxW={40}>
          <NumberInputField textAlign="right" fontSize="5xl" height={24} />
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
            fill-rule="evenodd"
            d="M10.5 6a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zm0 12a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z"
            clip-rule="evenodd"
          />
        </Icon>
        <NumberInput defaultValue={0} min={0} max={59} size="lg" maxW={40}>
          <NumberInputField textAlign="right" fontSize="5xl" height={24} />
          <NumberInputStepper>
            <NumberIncrementStepper />
            <NumberDecrementStepper />
          </NumberInputStepper>
        </NumberInput>
      </Flex>
      <Flex gap={8}>
        <Button
          width={24}
          disabled={state === "READY"}
          onClick={() => setState("READY")}
        >
          リセット
        </Button>
        <Button
          width={24}
          onClick={() =>
            setState((oldState) => (oldState === "READY" ? "RUNNING" : "READY"))
          }
        >
          {state === "READY" ? "スタート" : "ストップ"}
        </Button>
        <Button onClick={onOpen}>
          <Icon as={Cog6ToothIcon} />
        </Button>
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
      </Flex>
    </VStack>
  );
};
