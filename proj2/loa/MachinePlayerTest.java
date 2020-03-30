package loa;

import static loa.Piece.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Square.sq;
import static loa.Move.mv;

public class MachinePlayerTest {
    static final Piece[][] BOARD1 = {
            { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
            { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP  },
            { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP  },
            { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
            { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };
    static final Piece[][] BOARD2 = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };
    static final Piece[][] BOARD3 = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };



}
