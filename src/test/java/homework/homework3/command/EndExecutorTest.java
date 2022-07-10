package homework.homework3.command;

import homework.homework2.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static homework.homework3.util.MessageEnum.ERROR_RESULT;

class EndExecutorTest {
    @Mock
    private FileService fileService;
    private final EndExecutor endExecutor;

    public EndExecutorTest() {
        MockitoAnnotations.openMocks(this);
        endExecutor=new EndExecutor(fileService);
    }

    @Test
    void executeCmd_End_ExpectedBehavior() {
        Mockito.doNothing().when(fileService).save();
        Mockito.doNothing().when(fileService).clearAll();
        String save = endExecutor.executeCmd("save");
        String clear=endExecutor.executeCmd("clear");

        Mockito.verify(fileService,Mockito.times(1)).save();
        Mockito.verify(fileService,Mockito.times(1)).clearAll();
        Assertions.assertEquals("Изменения сохранены в соответсвующих файлах", save);
        Assertions.assertEquals("Все строки удалены из соответсвующих файлов", clear);
    }

    @Test
    void executeCmd_End_WrongValue() {
        String result=endExecutor.executeCmd("save_");
        Assertions.assertEquals(ERROR_RESULT.getMessage(),result);
    }
}