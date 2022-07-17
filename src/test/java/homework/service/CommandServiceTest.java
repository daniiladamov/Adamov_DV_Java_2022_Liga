package homework.service;

import homework.command.*;
import homework.util.CommandEnum;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

class CommandServiceTest {

    private CommandService commandService;
    @Mock
    private GetExecutor getExecutor;
    @Mock
    private PatchExecutor patchExecutor;
    @Mock
    private PostExecutor postExecutor;
    @Mock
    private DeleteExecutor deleteExecutor;
    @Mock
    private PutExecutor putExecutor;
    @Mock
    private CommandExecutor badExecutor;

    public CommandServiceTest(){
        MockitoAnnotations.openMocks(this);
        commandService=new CommandService(getExecutor,patchExecutor,postExecutor,deleteExecutor,putExecutor
                ,badExecutor);
    }

    @Test
    void getMessage_ExpectedBehavior() {
        String testMessage="testMessage";
        Arrays.stream(CommandEnum.values()).forEach(enums->{
            commandService.getMessage(enums,testMessage);
        });
        Mockito.verify(getExecutor,Mockito.times(1)).executeCmd(testMessage);
        Mockito.verify(patchExecutor,Mockito.times(1)).executeCmd(testMessage);
        Mockito.verify(putExecutor,Mockito.times(1)).executeCmd(testMessage);
        Mockito.verify(postExecutor,Mockito.times(1)).executeCmd(testMessage);
        Mockito.verify(deleteExecutor,Mockito.times(1)).executeCmd(testMessage);
        Mockito.verify(badExecutor,Mockito.times(1)).executeCmd(testMessage);
    }

    @Test
    void getMessage_With_Null_CommandEnum(){
        String testMessage="testMessage";
        commandService.getMessage(null,testMessage);
        Mockito.verify(badExecutor,Mockito.times(1)).executeCmd(testMessage);
    }


}