package entity.task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
