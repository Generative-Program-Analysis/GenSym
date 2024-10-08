(module
  (memory $0 2)
  (func $createBtree (param i32) (result i32) ;; createBtree(t), where t: degree of the btree
        (i32.const 0)
        (local.get 0)
        (i32.store)                     ;; store t at address 0
        (i32.const 0)
        (i32.const 1)
        (i32.store offset=4)                ;; store the number of nodes in the btree = 1
        (i32.const 0)
        (i32.const 65536)
        (i32.store offset=8)                ;; store the root addr
        (i32.const 1)                   ;; Now create root
        (memory.grow)                   ;; create new page
        (i32.const -1)
        (i32.ne)
        (if                         ;; if we can grow memory
          (then
            (i32.const 65536)             ;; 64KiB = 65536 bytes
            (i32.const 1)
            (i32.store)                 ;; store 1(TRUE) regarding if node is leaf
            (i32.const 65536)             ;; now store number of keys
            (i32.const 0)
            (i32.store offset=4)            ;; store number of keys = 0
            )
          )
        (i32.const 65536)   ;; returns the address of the root
        )
  ;; btreeSearch(x, k), where x: node address; k: key to search. Returns address of the key or -1 if key is not in tree.
  (func $btreeSearch (param i32) (param i32) (result i32) (local i32)
        (i32.const 0)
        (local.set 2)                   ;; i = 0
        (block $loop_break
               (loop $loop
                     (local.get 2)                  ;; i
                     (local.get 0)                  ;; x
                     (i32.load offset=4)                ;; x.children
                     (i32.const 1)
                     (i32.sub)                    ;; x.n -1
                     (i32.le_s)                   ;; i <= x.children-1
                     (if
                       (then
                         (local.get 1)              ;; k
                         (local.get 0)              ;; x
                         (i32.const 4)
                         (local.get 2)              ;; i
                         (i32.mul)                ;; i*4
                         (i32.add)                ;; x + i*4
                         (i32.load offset=8)            ;; x.keys[i]
                         (i32.gt_s)               ;; k > x.keys[i]
                         (if
                           (then
                             (i32.const 1)
                             (local.get 2)
                             (i32.add)
                             (local.set 2)          ;; i = i+1
                             (br $loop)
                             )
                           (else
                             (br $loop_break)
                             )
                           )
                         )
                       (else
                         (br $loop_break)
                         )
                       )
                     )
               )
        (local.get 2)                 ;; i
        (local.get 0)                 ;; x
        (i32.load offset=4)               ;; x.children
        (i32.const 1)
        (i32.sub)                   ;; x.n - 1
        (i32.le_s)                    ;; i <= x.children - 1
        (if (result i32)
          (then
            (local.get 1)             ;; k
            (local.get 0)             ;; x
            ;; x+4*i
            (i32.const 4)
            (local.get 2)             ;; i
            (i32.mul)               ;; i*4
            (i32.add)               ;; x + i*4
            (i32.load offset=8)           ;; x.keys[i]
            (i32.eq)
            ;; k == x.keys[i]
            (if (result i32)
              (then
                (local.get 0) ;; save the address, which is x+8+i*4
                (i32.const 8)
                (i32.add)           ;; x+8
                (i32.const 4)
                (local.get 2)         ;; i
                (i32.mul)           ;; i*4
                (i32.add)           ;; x+8+i*4
                )
              (else
                (local.get 0)             ;; x
                (i32.load)                ;; x is leaf?
                (i32.const 1)             ;; if == 1, then yes
                (i32.eq)
                (if (result i32)
                  (then
                    (i32.const -1)
                    )
                  (else
                    (i32.const 0)
                    (i32.load)            ;; t
                    (i32.const 1)
                    (i32.sub)           ;; t-1
                    (i32.const 4)
                    (i32.mul)           ;; (t-1)*4
                    (local.get 2)         ;; i
                    (i32.const 4)
                    (i32.mul)           ;; i*4
                    (i32.add)           ;; (t-1)*4 + i*4
                    (local.get 0)         ;; x
                    (i32.add)           ;; x + (t-1)*4 + i*4
                    (i32.load offset=8)       ;; load the address of the child in index i
                    (local.get 1)         ;; k
                    (call $btreeSearch)       ;; btreeSearch(x.children[i], k)
                    )
                  )
                )
              )
            )
          (else
            (local.get 0)             ;; x
            (i32.load)                ;; x is leaf?
            (i32.const 1)             ;; if == 1, then yes
            (i32.eq)
            (if (result i32)
              (then
                (i32.const -1)
                )
              (else
                (i32.const 0)
                (i32.load)            ;; t
                (i32.const 1)
                (i32.sub)           ;; t-1
                (i32.const 4)
                (i32.mul)           ;; (t-1)*4
                (local.get 2)         ;; i
                (i32.const 4)
                (i32.mul)           ;; i*4
                (i32.add)           ;; (t-1)*4 + i*4
                (local.get 0)         ;; x
                (i32.add)           ;; x + (t-1)*4 + i*4
                (i32.load offset=8)       ;; load the address of the child in index i
                (local.get 1)         ;; k
                (call $btreeSearch)       ;; btreeSearch(x.children[i], k)
                )
              )
            )
          )
        )
  ;; btreeSplitChild(x, i), where x: addr of a non full internal node; i: index such that x.children[i] is a full child of x
  (func $btreeSplitChild (param i32) (param i32) (local i32) (local i32)
        (i32.const 1) ;; create a new node
        (memory.grow)                   ;; create new page
        (i32.const -1)
        (i32.ne)
        (if                     ;; if we can grow memory
          (then ;; get the number of nodes in the tree
            (i32.const 0)
            (i32.load offset=4)             ;; number of nodes in the tree
            (i32.const 1)
            (i32.add)                 ;; number of nodes + 1
            (i32.const 65536)
            (i32.mul)                 ;; address is 64KiB*(number of nodes+1)
            (local.set 2)
            (i32.const 0) ;; change number of nodes
            (i32.const 0)
            (i32.load offset=4)
            (i32.const 1)
            (i32.add)
            (i32.store offset=4)            ;; set number of nodes to + 1
            (i32.const 0) ;; get x.children[i]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 1)         ;; i
            (i32.const 4)
            (i32.mul)           ;; i*4
            (i32.add)           ;; (t-1)*4 + i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4 + i*4
            (i32.load offset=8)       ;; load the addr of the child in index i : y
            (i32.load)            ;; check if x.children[i] is leaf ;; load the first i32 in the child
            (i32.const 1)
            (i32.eq)            ;; is y leaf?
            (if
              (then ;; yes
                (local.get 2)     ;; addr of new node z
                (i32.const 1)
                (i32.store)       ;; store 1(TRUE) regarding if node is leaf
                )
              (else ;; no
                (local.get 2)     ;; addr of new node z
                (i32.const 0)
                (i32.store)       ;; store 0(FALSE) regarding if node is leaf
                )
              )
            (local.get 2)               ;; now store number of children, which will be t/2 -1 ;; addr of z
            (i32.const 0)
            (i32.load)                  ;; t
            (i32.const 2)
            (i32.div_s)                 ;; t/2
            (i32.const 1)
            (i32.sub)                 ;; (t/2)-1
            (i32.store offset=4)            ;; store number of children = (t/2)-1
            (i32.const 0)   ;; now to store the keys
            (local.set 3)           ;; counter = 0
            (block $loop_break
                   (loop $loop
                         (local.get 3)          ;; counter
                         (i32.const 0)
                         (i32.load)           ;; t
                         (i32.const 2)
                         (i32.div_s)            ;; t/2
                         (i32.const 1)
                         (i32.sub)            ;; (t/2)-1
                         (i32.eq)           ;; counter == (t/2)-1
                         (if
                           (then
                             (br $loop_break)
                             )
                           (else
                             (local.get 2)          ;; addr of z ;; we will store in the new node's address. z.keys[counter]
                             (i32.const 4)
                             (local.get 3)          ;; counter
                             (i32.mul)            ;; counter * 4
                             (i32.add)            ;; addr + counter * 4 -> where the key is goint to be located
                             (i32.const 0)   ;; get x.children[i]
                             (i32.load)           ;; t
                             (i32.const 1)
                             (i32.sub)            ;; t-1
                             (i32.const 4)
                             (i32.mul)            ;; (t-1)*4
                             (local.get 1)          ;; i
                             (i32.const 4)
                             (i32.mul)            ;; i*4
                             (i32.add)            ;; (t-1)*4 + i*4
                             (local.get 0)          ;; x
                             (i32.add)            ;; x + (t-1)*4 + i*4
                             (i32.load offset=8)        ;; load the address of the child in index i = y
                             (i32.const 4)  ;; now get x.children[i].keys[counter+(t/2)]
                             (local.get 3)              ;; counter
                             (i32.const 0)
                             (i32.load)               ;; t
                             (i32.const 2)
                             (i32.div_s)                ;; t/2
                             (i32.add)                ;; counter + (t/2)
                             (i32.mul)                ;; counter + (t/2) *4
                             (i32.add)                ;; y + counter + (t/2) *4
                             (i32.load offset=8)            ;; y.keys[counter + (t/2)]
                             (i32.store offset=8)         ;; z.keys[counter] = y.keys[counter + (t/2)]
                             (local.get 3)   ;; increment counter
                             (i32.const 1)
                             (i32.add)
                             (local.set 3)
                             (br $loop)
                             )
                           )
                         )
                   )
            (i32.const 0)
            (local.set 3)   ;; set counter back to 0
            (i32.const 0)   ;; check if x.children[i] is leaf
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 1)         ;; i
            (i32.const 4)
            (i32.mul)           ;; i*4
            (i32.add)           ;; (t-1)*4 + i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4 + i*4
            (i32.load offset=8)       ;; load the address of the child in index i = y
            (i32.load)            ;; get first i32 in child --> isLeaf
            (i32.const 1)
            (i32.ne)            ;; is leaf?
            (if
              (then ;; not leaf so we have to update the children
                (block $loop_break
                       (loop $loop
                             (local.get 3)          ;; counter
                             (i32.const 0)
                             (i32.load)           ;; t
                             (i32.const 2)
                             (i32.div_s)            ;; t/2
                             (i32.eq)           ;; counter == (t/2)
                             (if
                               (then
                                 (br $loop_break)
                                 )
                               (else
                                 (i32.const 0)      ;; now get z.children[counter]
                                 (i32.load)           ;; t
                                 (i32.const 1)
                                 (i32.sub)            ;; t-1
                                 (i32.const 4)
                                 (i32.mul)            ;; (t-1)*4
                                 (local.get 3)          ;; counter
                                 (i32.const 4)
                                 (i32.mul)            ;; counter*4
                                 (i32.add)            ;; (t-1)*4 + counter*4
                                 (local.get 2)          ;; z
                                 (i32.add)            ;; z + (t-1)*4 + counter*4 --> address where addr of child is stored
                                 (i32.const 0)          ;; get x.children[i]
                                 (i32.load)           ;; t
                                 (i32.const 1)
                                 (i32.sub)            ;; t-1
                                 (i32.const 4)
                                 (i32.mul)            ;; (t-1)*4
                                 (local.get 1)          ;; i
                                 (i32.const 4)
                                 (i32.mul)            ;; i*4
                                 (i32.add)            ;; (t-1)*4 + i*4
                                 (local.get 0)          ;; x
                                 (i32.add)            ;; x + (t-1)*4
                                 (i32.load offset=8)        ;; load the address of the child in index i = y
                                 (i32.const 0)      ;; get x.children[i].children[counter+(t/2)]
                                 (i32.load)           ;; t
                                 (i32.const 1)
                                 (i32.sub)            ;; t-1
                                 (i32.const 4)
                                 (i32.mul)            ;; (t-1)*4
                                 (local.get 3)          ;; counter
                                 (i32.const 0)
                                 (i32.load)           ;; t
                                 (i32.const 2)
                                 (i32.div_s)            ;; t/2
                                 (i32.add)            ;; counter + t/2
                                 (i32.const 4)
                                 (i32.mul)            ;; counter+t/2  * 4
                                 (i32.add)            ;; (t-1)*4 + (counter+(t/2))*4
                                 (i32.add)            ;; x.children[i] + (t-1)*4 + (counter + (t/2))*4
                                 (i32.load offset=8)        ;; load the address of the child in index counter + t/2
                                 (i32.store offset=8) ;; z.children[counter] = x.children[i].children[counter]
                                 (local.get 3) ;; increment counter
                                 (i32.const 1)
                                 (i32.add)
                                 (local.set 3)
                                 (br $loop)
                                 )
                               )
                             )
                       )
                )
              )
            (i32.const 0)     ;; get x.children[i]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 1)         ;; i
            (i32.const 4)
            (i32.mul)           ;; i*4
            (i32.add)           ;; (t-1)*4 + i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4
            (i32.load offset=8)       ;; load the address of the child in index i = y
            (i32.const 0)
            (i32.load)            ;; t
            (i32.const 2)
            (i32.div_s)           ;; t/2
            (i32.const 1)
            (i32.sub)           ;; t/2 -1
            (i32.store offset=4)        ;; number of keys of x.children[i] is now t/2 -1
            (local.get 0)         ;; x
            (i32.load offset=4)       ;; get x.n
            (local.set 3)         ;; counter = x.n
            (block $loop_break
                   (loop $loop
                         (local.get 1)        ;; i
                         (local.get 3)        ;; counter
                         (i32.eq)
                         (if
                           (then
                             (br $loop_break)
                             )
                           (else
                             (i32.const 0)        ;; get x.children[counter + 1]
                             (i32.load)           ;; t
                             (i32.const 1)
                             (i32.sub)            ;; t-1
                             (i32.const 4)
                             (i32.mul)            ;; (t-1)*4
                             (local.get 3)          ;; counter
                             (i32.const 1)
                             (i32.add)            ;; counter + 1
                             (i32.const 4)
                             (i32.mul)            ;; (counter + 1)*4
                             (i32.add)            ;; (t-1)*4 + (counter + 1)*4
                             (local.get 0)          ;; x
                             (i32.add)            ;; x + (t-1)*4 + (counter + 1)*4 -->address where addr of child is stored
                             (i32.const 0)          ;; get x.children[counter]
                             (i32.load)           ;; t
                             (i32.const 1)
                             (i32.sub)            ;; t-1
                             (i32.const 4)
                             (i32.mul)            ;; (t-1)*4
                             (local.get 3)          ;; counter
                             (i32.const 4)
                             (i32.mul)            ;; (counter)*4
                             (i32.add)            ;; (t-1)*4 + (counter)*4
                             (local.get 0)          ;; x
                             (i32.add)            ;; x + (t-1)*4 + (counter)*4
                             (i32.load offset=8)        ;; load the addr of the child in index (counter)
                             (i32.store offset=8)           ;; x.children[counter+1] = x.children[counter]
                             (local.get 3)    ;; decrement counter
                             (i32.const 1)
                             (i32.sub)
                             (local.set 3)
                             (br $loop)
                             )
                           )
                         )
                   )
            (i32.const 0)     ;; get x.children[i + 1]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 1)         ;; i
            (i32.const 1)
            (i32.add)           ;; i + 1
            (i32.const 4)
            (i32.mul)           ;; (i + 1)*4
            (i32.add)           ;; (t-1)*4 + (i + 1)*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4 + (i + 1)*4 --> addr where we will store the new addr of the child
            (local.get 2)         ;; addr of z
            (i32.store offset=8)      ;; x.children[i+1] = z
            (local.get 0)         ;; x
            (i32.load offset=4)       ;; get x.n
            (i32.const 1)
            (i32.sub)
            (local.set 3)         ;; counter = x.n -1
            (block $loop_break
                   (loop $loop
                         (local.get 1)        ;; i
                         (i32.const 1)
                         (i32.sub)          ;; i-1
                         (local.get 3)        ;; counter
                         (i32.eq)         ;; counter == i-1
                         (if
                           (then
                             (br $loop_break)
                             )
                           (else
                             (local.get 3)          ;; get x.keys[counter + 1] ;; counter
                             (i32.const 1)
                             (i32.add)            ;; counter + 1
                             (i32.const 4)
                             (i32.mul)            ;; (counter + 1)*4
                             (local.get 0)          ;; x
                             (i32.add)            ;; x +(counter + 1)*4 --> addr where we will store the new key
                             (local.get 3)          ;; counter
                             (i32.const 4)
                             (i32.mul)            ;; (counter)*4
                             (local.get 0)          ;; x
                             (i32.add)            ;; x +(counter)*4
                             (i32.load offset=8)        ;; load the key in index (counter)
                             (i32.store offset=8)           ;; x.keys[counter+1] = x.keys[counter]
                             (local.get 3)  ;; decrement counter
                             (i32.const 1)
                             (i32.sub)
                             (local.set 3)
                             (br $loop)
                             )
                           )
                         )
                   )
            (local.get 1)   ;; get x.keys[i]
            (i32.const 4)
            (i32.mul)           ;; i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x +i*4 --> addr of the key
            (i32.const 0)  ;; get x.children[i].keys[t/2]
            (i32.load)            ;; t
            (i32.const 2)
            (i32.div_s)           ;; t/2
            (i32.const 1)
            (i32.sub)           ;; t/2 -1
            (i32.const 4)
            (i32.mul)           ;; (t/2)-1  *4
            (i32.const 0)  ;; get x.children[i]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 1)         ;; i
            (i32.const 4)
            (i32.mul)           ;; (i)*4
            (i32.add)           ;; (t-1)*4 + (i)*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4 + (i)*4
            (i32.load offset=8)       ;; load the address of the child in index (i)
            (i32.add)           ;; y + (t/2)-1 *4
            (i32.load offset=8)         ;; load the key in index (t/2)-1 from node x.children[i]
            (i32.store offset=8)      ;; store the new key in the addr
            (local.get 0)
            (local.get 0)         ;; x
            (i32.load offset=4)       ;; x.n
            (i32.const 1)
            (i32.add)           ;; x.n + 1
            (i32.store offset=4)      ;; x.n = x.n + 1
            )
          )
        )
  ;; btreeInsertNonFull(x, k), where x: addr of a non full internal node; k: the key to insert
  (func $btreeInsertNonFull (param i32) (param i32) (local i32)
        (local.get 0)     ;; x
        (i32.load offset=4)   ;; x.n
        (i32.const 1)
        (i32.sub)       ;; x.n -1
        (local.set 2)     ;; i = x.n -1
        (local.get 0)     ;; x
        (i32.load)        ;; get first i32 --> isLeaf
        (i32.const 1)
        (i32.eq)        ;; is leaf?
        (if
          (then   ;; x is leaf
            (block $loop_break
                   (loop $loop
                         (local.get 2)  ;; i
                         (i32.const 0)
                         (i32.ge_s)   ;; i <= 0?
                         (if (result i32)
                           (then
                             (local.get 1)              ;; k
                             (local.get 0)              ;; x
                             ;; x+4*i
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.mul)                ;; i*4
                             (i32.add)                ;; x + i*4
                             (i32.load offset=8)            ;; x.keys[i]
                             (i32.lt_s)               ;; k < x.keys[i]
                             )
                           (else
                             (i32.const 0)
                             )
                           )
                         (local.get 2)  ;; i
                         (i32.const 0)
                         (i32.ge_s)   ;; i <= 0?
                         (i32.and)
                         (if
                           (then
                             (local.get 0)              ;; x
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.const 1)
                             (i32.add)                ;; i+1
                             (i32.mul)                ;; i+1 *4
                             (i32.add)                ;; x + i+1 *4 --> addr of x.keys[i+1]
                             (local.get 0)              ;; x
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.mul)                ;; i *4
                             (i32.add)                ;; x + i *4 --> addr of x.keys[i]
                             (i32.load offset=8)            ;; x.keys[i]
                             (i32.store offset=8)         ;; x.keys[i+1] = x.keys[i]
                             (local.get 2)              ;; i
                             (i32.const 1)
                             (i32.sub)
                             (local.set 2)              ;; i = i-1
                             (br $loop)
                             )
                           (else
                             (br $loop_break)
                             )
                           )
                         )
                   )
            (local.get 0)             ;; x
            (i32.const 4)
            (local.get 2)             ;; i
            (i32.const 1)
            (i32.add)               ;; i+1
            (i32.mul)               ;; i+1 *4
            (i32.add)               ;; x + i+1 *4 --> addr of x.keys[i+1]
            (local.get 1)   ;; k
            (i32.store offset=8)  ;; x.keys[i+1] = k
            (local.get 0)
            (local.get 0) ;; x
            (i32.load offset=4) ;; x.n
            (i32.const 1)
            (i32.add)
            (i32.store offset=4)  ;; x.n = x.n +1
            )
          (else ;; x is not leaf
            (block $loop_break
                   (loop $loop
                         (local.get 2)  ;; i
                         (i32.const 0)
                         (i32.ge_s)   ;; i >= 0
                         (if
                           (then
                             (local.get 1)              ;; k
                             (local.get 0)              ;; x
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.mul)                ;; i*4
                             (i32.add)                ;; x + i*4
                             (i32.load offset=8)            ;; x.keys[i]
                             (i32.lt_s)               ;; k < x.keys[i]
                             (if
                               (then
                                 (local.get 2)  ;; i
                                 (i32.const 1)
                                 (i32.sub)    ;; i -1
                                 (local.set 2)  ;; i = i-1
                                 (br $loop)
                                 )
                               (else
                                 (br $loop_break)
                                 )
                               )
                             )
                           (else
                             (br $loop_break)
                             )
                           )
                         )
                   )
            (local.get 2)
            (i32.const 1)
            (i32.add)
            (local.set 2)         ;;i = i+1
            (i32.const 0) ;; get x.children[i]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 2)         ;; i
            (i32.const 4)
            (i32.mul)           ;; i*4
            (i32.add)           ;; (t-1)*4 + i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4 --> address of the child in index i = y
            (i32.load offset=8)       ;; load the address of the child in index i = y
            (i32.load offset=4)       ;; get y.n
            (i32.const 0)
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.eq)            ;; y.n == t-1 --> node is full
            (if
              (then
                (local.get 0) ;; x
                (local.get 2) ;; i
                (call $btreeSplitChild)
                (local.get 1)             ;; k
                (local.get 0)             ;; x
                (i32.const 4)
                (local.get 2)             ;; i
                (i32.mul)               ;; i*4
                (i32.add)               ;; x + i*4
                (i32.load offset=8)           ;; x.keys[i]
                (i32.gt_s)      ;; k>x.keys[i]
                (if
                  (then
                    (local.get 2) ;; i
                    (i32.const 1)
                    (i32.add)
                    (local.set 2) ;; i = i+1
                    )
                  )
                )
              )
            (i32.const 0)   ;; get x.children[i]
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (local.get 2)         ;; i
            (i32.const 4)
            (i32.mul)           ;; i*4
            (i32.add)           ;; (t-1)*4 + i*4
            (local.get 0)         ;; x
            (i32.add)           ;; x + (t-1)*4
            (i32.load offset=8)       ;; load the address of the child in index i = y
            (local.get 1)     ;; k
            (call $btreeInsertNonFull)
            )
          )
        )
  (func $btreeInsert (param i32) (result i32) (local i32) (local i32)
        (i32.const 0)
        (i32.load offset=8)     ;; root addr
        (local.tee 2)   ;; set r
        (i32.load offset=4)   ;; r.n
        (i32.const 0)
        (i32.load)            ;; t
        (i32.const 1)
        (i32.sub)           ;; t-1
        (i32.eq)    ;; r.n == t-1 --> root is full
        (if (result i32)
          (then
            (i32.const 1) ;; create a new node
            (memory.grow)                   ;; create new page
            (i32.const -1)
            (i32.ne)
            (if (result i32)                    ;; if we can grow memory
              (then
                (i32.const 0) ;; get the number of nodes in the tree
                (i32.load offset=4)             ;; number of nodes in the tree
                (i32.const 1)
                (i32.add)                 ;; number of nodes + 1
                (i32.const 65536)
                (i32.mul)                 ;; address  of s is 64KiB*(number of nodes+1)
                (local.set 1)
                (i32.const 0) ;; change number of nodes
                (i32.const 0)
                (i32.load offset=4)
                (i32.const 1)
                (i32.add)
                (i32.store offset=4)            ;; set number of nodes to + 1
                (i32.const 0)
                (local.get 1)   ;; s
                (i32.store offset=8)    ;; root addr is now s
                (local.get 1)   ;; s
                (i32.const 0)
                (i32.store)                 ;; store 0(FALSE) regarding if node is leaf
                (local.get 1) ;; now store number of keys
                (i32.const 0)
                (i32.store offset=4)            ;; store number of keys = 0
                (i32.const 0) ;; store children addresses
                (i32.load)            ;; t
                (i32.const 1)
                (i32.sub)           ;; t-1
                (i32.const 4)
                (i32.mul)           ;; (t-1)*4
                (i32.const 0)         ;; index = 0
                (i32.const 4)
                (i32.mul)           ;; i*4
                (i32.add)           ;; (t-1)*4 + i*4
                (local.get 1)         ;; x
                (i32.add)           ;; x + (t-1)*4 --> addre where addr of child number 0 is located
                (local.get 2)
                (i32.store offset=8)      ;; s.children[0] = r
                (local.get 1)   ;; s
                (i32.const 0)
                (call $btreeSplitChild)
                (local.get 1)   ;; s
                (local.get 0)   ;; k
                (call $btreeInsertNonFull)
                (local.get 1) ;; return the address of the new root
                )
              (else
                (i32.const -1)  ;; return -1 because we could not create a new node
                )
              )
            )
          (else
            (local.get 2) ;; r
            (local.get 0) ;; k
            (call $btreeInsertNonFull)
            (local.get 2) ;; return the address of the root, which is the same
            )
          )
        )
  ;; Returns the address of the new root (if applicable, otherwise returns the addres of the root)
  (func $btreeDelete (param i32) (param i32) (result i32) (local i32) (local i32) (local i32) (local i32)
        (local.get 0)     ;; x
        (i32.load)        ;; get first i32 --> isLeaf
        (i32.const 1)
        (i32.eq)        ;; is leaf?
        (if
          (then   ;; x is leaf
            (i32.const 0)
            (local.set 2) ;; i = 0
            (block $loop_break
                   (loop $loop
                         (local.get 0)      ;; x
                         (i32.load offset=4)    ;; x.n
                         (local.get 2)      ;; i
                         (i32.eq)       ;; i == x.n
                         (if
                           (then
                             (br $loop_break) ;; break from loop
                             )
                           (else
                             (local.get 0)              ;; x
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.mul)                ;; i*4
                             (i32.add)                ;; x + i*4
                             (i32.load offset=8)            ;; x.keys[i]
                             (local.get 1)    ;; k
                             (i32.eq)     ;; k == x.keys[i]
                             (if
                               (then
                                 (local.get 2)  ;; i
                                 (local.set 3)  ;; j = i
                                 (block $while_break
                                        (loop $while
                                              (local.get 3)     ;; j
                                              (local.get 0)     ;; x
                                              (i32.load offset=4)   ;; x.n
                                              (i32.const 1)
                                              (i32.sub)       ;; x.n-1
                                              (i32.eq)        ;; j == x.n-1
                                              (if
                                                (then
                                                  (br $while_break)
                                                  )
                                                (else
                                                  (local.get 0)             ;; x
                                                  (i32.const 4)
                                                  (local.get 3)             ;; j
                                                  (i32.mul)               ;; j*4
                                                  (i32.add)               ;; x + j*4 --> addr where x.keys[j] is stored
                                                  (local.get 0)             ;; x
                                                  (i32.const 4)
                                                  (local.get 3)             ;; j
                                                  (i32.const 1)
                                                  (i32.add)               ;; j+1
                                                  (i32.mul)               ;; j+1 *4
                                                  (i32.add)               ;; x + j+1 *4 --> addr where x.keys[j+1] is stored
                                                  (i32.load offset=8)           ;; x.keys[j+1]
                                                  (i32.store offset=8)            ;; x.keys[j] = x.keys[j+1]
                                                  (local.get 3)
                                                  (i32.const 1)
                                                  (i32.add)
                                                  (local.set 3)   ;; j = j+1
                                                  (br $while)
                                                  )
                                                )
                                              )
                                        )
                                 (local.get 0)      ;; x
                                 (local.get 0)      ;; x
                                 (i32.load offset=4)    ;; x.n
                                 (i32.const 1)
                                 (i32.sub)
                                 (i32.store offset=4) ;; x.n = x.n -1
                                 (br $loop_break)
                                 )
                               )
                             )
                           )
                         (i32.const 1)
                         (local.get 2)
                         (i32.add)
                         (local.set 2)  ;; i = i+1
                         (br $loop)   ;; continue
                         )
                   )
            )
          (else ;; x is not leaf
            (i32.const 0) ;;get the index of the appropriate child/key
            (local.set 2)     ;; i = 0
            (block $loop_break
                   (loop $loop
                         (local.get 2)  ;; i
                         (local.get 0)      ;; x
                         (i32.load offset=4)    ;; x.n
                         (i32.const 1)
                         (i32.sub)
                         (i32.le_s)   ;; i <= x.n - 1
                         (if (result i32)
                           (then
                             (local.get 1)  ;; k
                             (local.get 0)              ;; x
                             (i32.const 4)
                             (local.get 2)              ;; i
                             (i32.mul)                ;; i*4
                             (i32.add)                ;; x + i*4
                             (i32.load offset=8)            ;; x.keys[i]
                             (i32.gt_s)   ;; k > x.keys[i]
                             )
                           (else
                             (i32.const 0)
                             )
                           )
                         (local.get 2)  ;; i
                         (local.get 0)      ;; x
                         (i32.load offset=4)    ;; x.n
                         (i32.const 1)
                         (i32.sub)
                         (i32.le_s)   ;; i <= x.n - 1
                         (i32.and)    ;; i <= x.n - 1 && k > x.keys[i]
                         (if
                           (then
                             (local.get 2)  ;; i
                             (i32.const 1)
                             (i32.add)
                             (local.set 2)  ;; i = i + 1
                             (br $loop)
                             )
                           (else
                             (br $loop_break)
                             )
                           )
                         )
                   )
            (local.get 2)
            (local.get 0)
            (i32.load offset=4)   ;; x.n
            (i32.lt_s)    ;; i < x.n
            (if (result i32)
              (then
                (local.get 0)             ;; x
                (i32.const 4)
                (local.get 2)             ;; i
                (i32.mul)               ;; i*4
                (i32.add)               ;; x + i*4
                (i32.load offset=8)           ;; x.keys[i]
                (local.get 1)       ;; k
                (i32.eq)    ;; k == x.keys[i]
                )
              (else
                (i32.const 0)
                )
              )
            (if
              (then ;; key is present in node x
                (i32.const 0)
                (i32.load)            ;; t
                (i32.const 1)
                (i32.sub)           ;; t-1
                (i32.const 4)
                (i32.mul)           ;; (t-1)*4
                (local.get 2)         ;; i
                (i32.const 4)
                (i32.mul)           ;; i*4
                (i32.add)           ;; (t-1)*4 + i*4
                (local.get 0)         ;; x
                (i32.add)           ;; x + (t-1)*4 --> address where addr of child in index i is located
                (i32.load offset=8)       ;; x.c[i]
                (local.tee 5)
                (i32.load offset=4)       ;; x.c[i].n
                (i32.const 0)
                (i32.load)            ;; t
                (i32.const 2)
                (i32.div_s)           ;; t/2
                (i32.ge_s)            ;; x.c[i].n >= t/2
                (if
                  (then
                    (local.get 0)             ;; x
                    (i32.const 4)
                    (local.get 2)             ;; i
                    (i32.mul)               ;; i*4
                    (i32.add)               ;; x + i*4 ;; addr of x.keys[i]
                    (local.get 5)     ;; x.c[i]
                    (i32.const 4)
                    (local.get 5)
                    (i32.load offset=4)   ;; x.c[i].n
                    (i32.const 1)
                    (i32.sub)       ;; index = x.c[i].n - 1
                    (i32.mul)       ;; i*4
                    (i32.add)       ;; x + i*4
                    (i32.load offset=8)   ;; x.c[i].keys[x.c[i].n - 1]
                    (local.get 5)     ;; x.c[i]
                    (local.get 5)     ;; x.c[i]
                    (i32.const 4)
                    (local.get 5)
                    (i32.load offset=4)   ;; x.c[i].n
                    (i32.const 1)
                    (i32.sub)       ;; index = x.c[i].n - 1
                    (i32.mul)       ;; i*4
                    (i32.add)       ;; x + i*4
                    (i32.load offset=8)   ;; x.c[i].keys[x.c[i].n - 1]
                    (call $btreeDelete)   ;; (x.c[i], x.c[i].keys[x.c[i].n - 1])
                    (drop)
                    (i32.store offset=8)  ;; x.keys[i] = x.c[i].keys[x.c[i].n - 1]
                    )
                  (else
                    (i32.const 0)
                    (i32.load)            ;; t
                    (i32.const 1)
                    (i32.sub)           ;; t-1
                    (i32.const 4)
                    (i32.mul)           ;; (t-1)*4
                    (local.get 2)         ;; i
                    (i32.const 1)
                    (i32.add)           ;; i+1
                    (i32.const 4)
                    (i32.mul)           ;; i+1 *4
                    (i32.add)           ;; (t-1)*4 + i+1 *4
                    (local.get 0)         ;; x
                    (i32.add)           ;; x + (t-1)*4 --> address where addr of child in index i is located
                    (i32.load offset=8)       ;; x.c[i+1]
                    (local.tee 5)
                    (i32.load offset=4)       ;; x.c[i+1].n
                    (i32.const 0)
                    (i32.load)            ;; t
                    (i32.const 2)
                    (i32.div_s)           ;; t/2
                    (i32.ge_s)            ;; x.c[i+1].n >= t/2
                    (if
                      (then
                        (local.get 0)     ;; x
                        (i32.const 4)
                        (local.get 2)     ;; i
                        (i32.mul)       ;; i*4
                        (i32.add)       ;; x + i*4 ;; addr of x.keys[i]
                        (local.get 5)     ;; x.c[i+1]
                        (i32.const 4)
                        (i32.const 0)     ;; index = 0
                        (i32.mul)       ;; i *4
                        (i32.add)       ;; x + i*4
                        (i32.load offset=8)   ;; x.c[i+1].keys[0]
                        (local.get 5)   ;; x.c[i+1]
                        (local.get 5)     ;; x.c[i+1]
                        (i32.const 4)
                        (i32.const 0)     ;; index = 0
                        (i32.mul)       ;; i *4
                        (i32.add)       ;; x + i*4
                        (i32.load offset=8)   ;; x.c[i+1].keys[0]
                        (call $btreeDelete)
                        (drop)
                        (i32.store offset=8)  ;; x.keys[i] = x.c[i+1].keys[0]
                        )
                      (else
                        (i32.const 0)
                        (i32.load)            ;; t
                        (i32.const 1)
                        (i32.sub)           ;; t-1
                        (i32.const 4)
                        (i32.mul)           ;; (t-1)*4
                        (local.get 2)         ;; i
                        (i32.const 4)
                        (i32.mul)           ;; i*4
                        (i32.add)           ;; (t-1)*4 + i*4
                        (local.get 0)         ;; x
                        (i32.add)           ;; x + (t-1)*4 --> address where addr of child in index i is located
                        (i32.load offset=8)       ;; x.c[i]
                        (local.set 5)
                        (local.get 5)     ;; x.c[i]
                        (i32.const 4)
                        (local.get 5)
                        (i32.load offset=4)   ;; index = x.c[i].n
                        (i32.mul)       ;; i *4
                        (i32.add)       ;; x + i*4 --> addr where x.c[i].keys[x.c[i].n] will be stored
                        (local.get 1)   ;; k
                        (i32.store offset=8) ;; x.c[i].keys[x.c[i].n] = k
                        (i32.const 0)   ;; now merge x.c[i] with x.c[i+1]
                        (local.set 3) ;; j = 0
                        (block $loop_break
                               (loop $loop
                                     (local.get 3)   ;; j
                                     (i32.const 0)  ;;get x.c[i+1]
                                     (i32.load)           ;; t
                                     (i32.const 1)
                                     (i32.sub)            ;; t-1
                                     (i32.const 4)
                                     (i32.mul)            ;; (t-1)*4
                                     (local.get 2)          ;; i
                                     (i32.const 1)
                                     (i32.add)            ;; i+1
                                     (i32.const 4)
                                     (i32.mul)            ;; i+1 *4
                                     (i32.add)            ;; (t-1)*4 + i+1 *4
                                     (local.get 0)          ;; x
                                     (i32.add)            ;; x + (t-1)*4
                                     (i32.load offset=8)        ;; x.c[i+1]
                                     (i32.load offset=4)        ;; x.c[i+1].n
                                     (i32.eq)
                                     (if      ;; j == x.c[i+1].n
                                       (then
                                         (br $loop_break)
                                         )
                                       (else
                                         (local.get 5)  ;; x.c[i]
                                         (i32.const 4)
                                         (local.get 5)
                                         (i32.load offset=4)
                                         (local.get 3)
                                         (i32.add)
                                         (i32.const 1)
                                         (i32.add)        ;; index = x.c[i].n + j + 1
                                         (i32.mul)        ;; i*4
                                         (i32.add)        ;; x + i*4 ;; addr of x.c[i].keys[index]
                                         (i32.const 0)  ;;get x.c[i+1]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 2)          ;; i
                                         (i32.const 1)
                                         (i32.add)            ;; i+1
                                         (i32.const 4)
                                         (i32.mul)            ;; i+1 *4
                                         (i32.add)            ;; (t-1)*4 + i+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4
                                         (i32.load offset=8)      ;; x.c[i+1]
                                         (i32.const 4)
                                         (local.get 3)      ;; index = j
                                         (i32.mul)        ;; i*4
                                         (i32.add)        ;; x.c[i+1] + i*4 ;; addr of x.c[i+1].keys[j]
                                         (i32.load offset=8)    ;; x.c[i+1].keys[j]
                                         (i32.store offset=8) ;; x.c[i].keys[index] = x.c[i+1].keys[j]
                                         (local.get 3)
                                         (i32.const 1)
                                         (i32.add)
                                         (local.set 3)  ;; j = j+1
                                         (br $loop)
                                         )
                                       )
                                     )
                               )
                        (local.get 5)
                        (local.get 5)
                        (i32.load offset=4) ;; x.c[i].n
                        (i32.const 1)
                        (i32.add)   ;; x.c[i].n +1
                        (i32.const 0) ;;get x.c[i+1]
                        (i32.load)            ;; t
                        (i32.const 1)
                        (i32.sub)           ;; t-1
                        (i32.const 4)
                        (i32.mul)           ;; (t-1)*4
                        (local.get 2)         ;; i
                        (i32.const 1)
                        (i32.add)           ;; i+1
                        (i32.const 4)
                        (i32.mul)           ;; i+1 *4
                        (i32.add)           ;; (t-1)*4 + i+1 *4
                        (local.get 0)       ;; x
                        (i32.add)         ;; x + (t-1)*4
                        (i32.load offset=8)     ;; x.c[i+1]
                        (i32.load offset=4)     ;; x.c[i+1].n
                        (i32.add)
                        (i32.store offset=4)    ;; x.c[i].n = x.c[i].n + 1 + x.c[i+1].n
                        (local.get 5)
                        (i32.load)
                        (i32.const 1)
                        (i32.ne)
                        (if
                          (then
                            (i32.const 0) ;; now adjust the children
                            (local.set 3) ;; j = 0
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)      ;; j
                                         (i32.const 0)  ;;get x.c[i+1]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 2)          ;; i
                                         (i32.const 1)
                                         (i32.add)            ;; i+1
                                         (i32.const 4)
                                         (i32.mul)            ;; i+1 *4
                                         (i32.add)            ;; (t-1)*4 + i+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4
                                         (i32.load offset=8)      ;; x.c[i+1]
                                         (i32.load offset=4)      ;; x.c[i+1].n
                                         (i32.const 1)
                                         (i32.add)
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (i32.const 0)  ;;get x.c[i].c[x.c[i].n+j]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 5)
                                             (i32.load offset=4)      ;; x.c[i].n
                                             (local.get 3)        ;; j
                                             (i32.add)          ;; index = x.c[i].n+j
                                             (i32.const 1)
                                             (i32.add)            ;; i+1
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 5)        ;; x.c[i]
                                             (i32.add)          ;; x.c[i] + (t-1)*4 --> address where x.c[i].c[x.c[i].n+j] is stored
                                             (i32.const 0)  ;;get x.c[i+1].c[j]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 3)          ;; j
                                             (i32.const 4)
                                             (i32.mul)            ;; j *4
                                             (i32.add)            ;; (t-1)*4 + j *4
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)            ;; i+1
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.add)          ;; x.c[i+1] + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1].c[j]
                                             (i32.store offset=8) ;; x.c[i].c[x.c[i].n+j] = x.c[i+1].c[j]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)  ;; j = j+1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            )
                          )
                        (local.get 2)   ;; i
                        (local.set 3)   ;; j = i
                        (block $loop_break
                               (loop $loop
                                     (local.get 0)
                                     (i32.load offset=4)
                                     (i32.const 1)
                                     (i32.sub)
                                     (local.get 3)
                                     (i32.eq)
                                     (if
                                       (then
                                         (br $loop_break)
                                         )
                                       (else
                                         (local.get 0)              ;; x
                                         (i32.const 4)
                                         (local.get 3)              ;; j
                                         (i32.mul)                ;; j*4
                                         (i32.add)                ;; x + j*4 ;; addr of x.keys[j]
                                         (local.get 0)              ;; x
                                         (i32.const 4)
                                         (local.get 3)              ;; j
                                         (i32.const 1)
                                         (i32.add)
                                         (i32.mul)                ;; j+1*4
                                         (i32.add)                ;; x + j+1*4 ;; addr of x.keys[j+1]
                                         (i32.load offset=8)
                                         (i32.store offset=8)   ;; x.keys[j] = x.keys[j+1]
                                         (local.get 3)
                                         (i32.const 1)
                                         (i32.add)
                                         (local.set 3)  ;; j = j+1
                                         (br $loop)
                                         )
                                       )
                                     )
                               )
                        (local.get 2)
                        (i32.const 1)
                        (i32.add)
                        (local.set 3) ;; j = i+1
                        (block $loop_break
                               (loop $loop
                                     (local.get 0)
                                     (i32.load offset=4)
                                     (local.get 3)
                                     (i32.eq)
                                     (if
                                       (then
                                         (br $loop_break)
                                         )
                                       (else
                                         (i32.const 0) ;;get x.c[j]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 3)          ;; j
                                         (i32.const 4)
                                         (i32.mul)            ;; j *4
                                         (i32.add)            ;; (t-1)*4 + j+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4 --> addr of x.c[j]
                                         (i32.const 0)  ;;get x.c[j+1]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 3)          ;; j
                                         (i32.const 1)
                                         (i32.add)            ;; j+1
                                         (i32.const 4)
                                         (i32.mul)            ;; i+1 *4
                                         (i32.add)            ;; (t-1)*4 + i+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4 --> addr of x.c[j+1]
                                         (i32.load offset=8)
                                         (i32.store offset=8)   ;; x.c[j] = x.c[j+1]
                                         (local.get 3)
                                         (i32.const 1)
                                         (i32.add)
                                         (local.set 3)  ;; j = j+1
                                         (br $loop)
                                         )
                                       )
                                     )
                               )
                        (local.get 0)
                        (local.get 0)
                        (i32.load offset=4) ;; x.n
                        (i32.const 1)
                        (i32.sub)
                        (i32.store offset=4)  ;; x.n = x.n-1
                        (local.get 5)   ;; x.c[i]
                        (local.get 1)   ;; k
                        (call $btreeDelete)
                        (drop)
                        )
                      )
                    )
                  )
                )
              (else ;; key not present in node x
                (i32.const 0)
                (i32.load)            ;; t
                (i32.const 1)
                (i32.sub)           ;; t-1
                (i32.const 4)
                (i32.mul)           ;; (t-1)*4
                (local.get 2)         ;; i
                (i32.const 4)
                (i32.mul)           ;; i*4
                (i32.add)           ;; (t-1)*4 + i*4
                (local.get 0)         ;; x
                (i32.add)           ;; x + (t-1)*4 --> address where addr of child in index i is located
                (i32.load offset=8)       ;; x.c[i]
                (local.set 5)
                (local.get 5)
                (i32.load offset=4)   ;; x.c[i].n
                (i32.const 0)
                (i32.load)            ;; t
                (i32.const 2)
                (i32.div_s)
                (i32.const 1)
                (i32.sub)   ;; t/2 - 1
                (i32.eq)
                (if
                  (then
                    (i32.const -1)
                    (local.set 4)   ;; changed = -1
                    (local.get 2) ;; i
                    (i32.const 1)
                    (i32.add)   ;; i+1
                    (local.get 0)
                    (i32.load offset=4) ;; x.n
                    (i32.le_s)    ;; i+1 <= x.n
                    (if
                      (then
                        (i32.const 0) ;;get x.c[i+1]
                        (i32.load)            ;; t
                        (i32.const 1)
                        (i32.sub)           ;; t-1
                        (i32.const 4)
                        (i32.mul)           ;; (t-1)*4
                        (local.get 2)         ;; i
                        (i32.const 1)
                        (i32.add)
                        (i32.const 4)
                        (i32.mul)           ;; i+1 *4
                        (i32.add)           ;; (t-1)*4 + i+1 *4
                        (local.get 0)       ;; x
                        (i32.add)         ;; x + (t-1)*4
                        (i32.load offset=8)
                        (i32.load offset=4)     ;; x.c[i+1].n
                        (i32.const 0)
                        (i32.load)            ;; t
                        (i32.const 2)
                        (i32.div_s)     ;; t/2
                        (i32.ge_s)  ;; x.c[i+1].n >= t/2
                        (if
                          (then
                            (local.get 5)     ;; x.c[i]
                            (i32.const 4)
                            (i32.const 0)
                            (i32.load)        ;; t
                            (i32.const 2)
                            (i32.div_s)       ;; t/2
                            (i32.const 1)
                            (i32.sub)       ;; index = t/2 -1
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i].keys[t/2-1]
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; i
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i]
                            (i32.load offset=8)   ;; x.keys[i]
                            (i32.store offset=8)    ;; x.c[i].keys[t/2-1] = x.keys[i]
                            (local.get 5)
                            (local.get 5)
                            (i32.load offset=4) ;; x.c[i].n
                            (i32.const 1)
                            (i32.add)
                            (i32.store offset=4)  ;;x.c[i].n = x.c[i].n +1
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; i
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i]
                            (i32.const 0) ;;get x.c[i+1]
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.add)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i+1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i+1]
                            (i32.const 4)
                            (i32.const 0)     ;; index = 0
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i+1].keys[0]
                            (i32.load offset=8)   ;; x.c[i+1].keys[0]
                            (i32.store offset=8)  ;; x.keys[i] = x.c[i+1].keys[0]
                            (local.get 5)
                            (i32.load)      ;; x.c[i] is leaf?
                            (i32.const 1)
                            (i32.ne)
                            (if       ;; if x.c[i] is not leaf, we have to adjust children
                              (then
                                (i32.const 0) ;;get x.c[i].c[t/2]
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (i32.const 0)
                                (i32.load)            ;; t
                                (i32.const 2)
                                (i32.div_s)           ;; index = t/2
                                (i32.const 4)
                                (i32.mul)           ;; i+1 *4
                                (i32.add)           ;; (t-1)*4 + i+1 *4
                                (local.get 5)       ;; x.c[i]
                                (i32.add)         ;; x + (t-1)*4 --> addr of x.c[i].c[t/2]
                                (i32.const 0) ;;get x.c[i+1].c[0]
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (i32.const 0)         ;; index = 0
                                (i32.const 4)
                                (i32.mul)           ;; i+1 *4
                                (i32.add)           ;; (t-1)*4 + i+1 *4
                                (i32.const 0)
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (local.get 2)         ;; i
                                (i32.const 1)
                                (i32.add)
                                (i32.const 4)
                                (i32.mul)           ;; i+1 *4
                                (i32.add)           ;; (t-1)*4 + i+1 *4
                                (local.get 0)       ;; x
                                (i32.add)         ;; x + (t-1)*4
                                (i32.load offset=8)     ;; x.c[i+1]
                                (i32.add)         ;; x + (t-1)*4
                                (i32.load offset=8)     ;; x.c[i+1].c[0]
                                (i32.store offset=8)   ;; x.c[i].c[t/2] = x.c[i+1].c[0]
                                )
                              )
                            (local.get 5)     ;; x.c[i]
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 2)
                            (i32.div_s)           ;; t/2
                            (i32.store offset=4)    ;; x.c[i].n = t/2
                            (i32.const 0)
                            (local.set 3)   ;; j = 0
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)    ;; j
                                         (i32.const 0)  ;;get x.c[i+1]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 2)          ;; i
                                         (i32.const 1)
                                         (i32.add)
                                         (i32.const 4)
                                         (i32.mul)            ;; i+1 *4
                                         (i32.add)            ;; (t-1)*4 + i+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4
                                         (i32.load offset=8)      ;; x.c[i+1]
                                         (i32.load offset=4)      ;; x.c[i+1].n
                                         (i32.const 1)
                                         (i32.sub)
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.const 4)
                                             (local.get 3)      ;; index = j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 --> address where x.c[i+1].keys[j] is located
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.const 4)
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)        ;; index = j+1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4
                                             (i32.load offset=8)    ;; x.c[i+1].keys[j+1]
                                             (i32.store offset=8) ;; x.c[i+1].keys[j] = x.c[i+1].keys[j+1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)  ;; j = j+1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 5)
                            (i32.load)
                            (i32.const 1)
                            (i32.ne)
                            (if     ;; if x.c[i] is not leaf, we have to adjust children
                              (then
                                (i32.const 0)
                                (local.set 3)   ;; j = 0
                                (block $loop_break
                                       (loop $loop
                                             (local.get 3)    ;; j
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.load offset=4)      ;; x.c[i+1].n
                                             (i32.eq)
                                             (if
                                               (then
                                                 (br $loop_break)
                                                 )
                                               (else
                                                 (i32.const 0)  ;;get x.c[i+1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)          ;; index = j
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i+1 *4
                                                 (i32.const 0)  ;;get x.c[i+1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 2)          ;; i
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i+1 *4
                                                 (local.get 0)        ;; x
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i+1]
                                                 (i32.add)          ;; x + (t-1)*4 --> address where x.c[i+1].c[j] is located
                                                 (i32.const 0)  ;;get x.c[i+1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)
                                                 (i32.const 1)
                                                 (i32.add)            ;; index = j+1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i+1 *4
                                                 (i32.const 0)  ;;get x.c[i+1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 2)          ;; i
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i+1 *4
                                                 (local.get 0)        ;; x
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i+1]
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)    ;; x.c[i+1].c[j+1]
                                                 (i32.store offset=8) ;; x.c[i+1].c[j] = x.c[i+1].c[j+1]
                                                 (local.get 3)
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (local.set 3)  ;; j = j+1
                                                 (br $loop)
                                                 )
                                               )
                                             )
                                       )
                                )
                              )
                            (i32.const 0) ;;get x.c[i+1]
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.add)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i+1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4 --> addr where x.c[i+1] is located
                            (i32.load offset=8)
                            (i32.const 0) ;;get x.c[i+1]
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.add)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i+1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i+1]
                            (i32.load offset=4)     ;; x.c[i+1].n
                            (i32.const 1)
                            (i32.sub)
                            (i32.store offset=4)  ;; x.c[i+1].n = x.c[i+1].n - 1
                            (i32.const 0)
                            (local.set 4) ;; changed = 0
                            )
                          )
                        )
                      )
                    (local.get 4)   ;; changed
                    (i32.const -1)
                    (i32.eq)
                    (local.get 2) ;; i
                    (i32.const 1)
                    (i32.sub)   ;; i - 1
                    (i32.const 0)
                    (i32.ge_s)    ;; i -1 >= 0
                    (i32.and)   ;; changed == -1 && i -1 >= 0
                    (if
                      (then
                        (i32.const 0) ;;get x.c[i-1]
                        (i32.load)            ;; t
                        (i32.const 1)
                        (i32.sub)           ;; t-1
                        (i32.const 4)
                        (i32.mul)           ;; (t-1)*4
                        (local.get 2)         ;; i
                        (i32.const 1)
                        (i32.sub)
                        (i32.const 4)
                        (i32.mul)           ;; i-1 *4
                        (i32.add)           ;; (t-1)*4 + i-1 *4
                        (local.get 0)       ;; x
                        (i32.add)         ;; x + (t-1)*4
                        (i32.load offset=8)
                        (i32.load offset=4)     ;; x.c[i-1].n
                        (i32.const 0)
                        (i32.load)            ;; t
                        (i32.const 2)
                        (i32.div_s)     ;; t/2
                        (i32.ge_s)  ;; x.c[i-1].n >= t/2
                        (if
                          (then
                            (local.get 5)
                            (i32.load offset=4)     ;; x.c[i].n
                            (local.set 3)       ;; j = x.c[i].n
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)    ;; j
                                         (i32.const 0)
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (local.get 5)      ;; x.c[i]
                                             (i32.const 4)
                                             (local.get 3)      ;; j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.c[i].keys[j]
                                             (local.get 5)      ;; x.c[i]
                                             (i32.const 4)
                                             (local.get 3)      ;; j
                                             (i32.const 1)
                                             (i32.sub)        ;; j-1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.c[i].keys[j-1]
                                             (i32.load offset=8)    ;; x.c[i].keys[j-1]
                                             (i32.store offset=8) ;; x.c[i].keys[j] = x.c[i].keys[j-1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.sub)
                                             (local.set 3)  ;; j = j-1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 5)
                            (i32.load)
                            (i32.const 1)
                            (i32.ne)
                            (if
                              (then
                                (local.get 5)
                                (i32.load offset=4)     ;; x.c[i].n
                                (i32.const 1)
                                (i32.add)
                                (local.set 3)       ;; j = x.c[i].n + 1
                                (block $loop_break
                                       (loop $loop
                                             (local.get 3)    ;; j
                                             (i32.const 0)
                                             (i32.eq)
                                             (if
                                               (then
                                                 (br $loop_break)
                                                 )
                                               (else
                                                 (i32.const 0)  ;;get x.c[i].c[j]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)          ;; j
                                                 (i32.const 4)
                                                 (i32.mul)            ;; j *4
                                                 (i32.add)            ;; (t-1)*4 + j *4
                                                 (local.get 5)        ;; x.c[i]
                                                 (i32.add)          ;; x + (t-1)*4 --> address where x.c[i].c[j] is located
                                                 (i32.const 0)  ;;get x.c[i].c[j-1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)          ;; j
                                                 (i32.const 1)
                                                 (i32.sub)            ;; j-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; j *4
                                                 (i32.add)            ;; (t-1)*4 + j *4
                                                 (local.get 5)        ;; x.c[i]
                                                 (i32.add)
                                                 (i32.load offset=8)      ;; x.c[i].c[j-1]
                                                 (i32.store offset=8)   ;; x.c[i].c[j] = x.c[i].c[j-1]
                                                 (local.get 3)
                                                 (i32.const 1)
                                                 (i32.sub)
                                                 (local.set 3)  ;; j = j-1
                                                 (br $loop)
                                                 )
                                               )
                                             )
                                       )
                                )
                              )
                            (local.get 5)
                            (local.get 5)
                            (i32.load offset=4)   ;; x.c[i].n
                            (i32.const 1)
                            (i32.add)
                            (i32.store offset=4) ;; ;; x.c[i].n = x.c[i].n + 1
                            (local.get 5)     ;; x.c[i]
                            (i32.const 4)
                            (i32.const 0)     ;; index = 0
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i].keys[0]
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; i
                            (i32.const 1)
                            (i32.sub)       ;; index = i-1
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i-1]
                            (i32.load offset=8)   ;; x.keys[i-1]
                            (i32.store offset=8)  ;; x.c[i].keys[0] = x.keys[i-1]
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i-1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i-1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)
                            (i32.load offset=4)     ;; x.c[i-1].n
                            (i32.const 1)
                            (i32.sub)
                            (i32.store offset=4)    ;; x.c[i-1].n = x.c[i-1].n - 1
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; i
                            (i32.const 1)
                            (i32.sub)       ;; index = i-1
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i-1]
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i-1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.const 4)
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i-1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.load offset=4)     ;; index = x.c[i-1].n
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i-1].keys[x.c[i-1].n]
                            (i32.load offset=8)
                            (i32.store offset=8)  ;; x.keys[i-1] = x.c[i-1].keys[x.c[i-1].n]
                            (local.get 5)
                            (i32.load)
                            (i32.const 1)
                            (i32.ne)
                            (if
                              (then
                                (i32.const 0) ;; get x.c[i].c[0]
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (i32.const 0)         ;; index = 0
                                (i32.const 4)
                                (i32.mul)           ;; i *4
                                (i32.add)           ;; (t-1)*4 + i *4
                                (local.get 5)       ;; x.c[i]
                                (i32.add)         ;; x + (t-1)*4 ;; addr where x.c[i].c[0] is located
                                (i32.const 0) ;; get x.c[i-1].c[x.c[i-1].n + 1]
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (i32.const 0)
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (local.get 2)         ;; i
                                (i32.const 1)
                                (i32.sub)
                                (i32.const 4)
                                (i32.mul)           ;; i-1 *4
                                (i32.add)           ;; (t-1)*4 + i-1 *4
                                (local.get 0)       ;; x
                                (i32.add)         ;; x + (t-1)*4
                                (i32.load offset=8)     ;; x.c[i-1]
                                (i32.load offset=4)     ;; x.c[i-1].n
                                (i32.const 1)
                                (i32.add)         ;; index = x.c[i-1.n + 1]
                                (i32.const 4)
                                (i32.mul)           ;; i-1 *4
                                (i32.add)           ;; (t-1)*4 + i-1 *4
                                (i32.const 0) ;;get x.c[i-1]
                                (i32.load)            ;; t
                                (i32.const 1)
                                (i32.sub)           ;; t-1
                                (i32.const 4)
                                (i32.mul)           ;; (t-1)*4
                                (local.get 2)         ;; i
                                (i32.const 1)
                                (i32.sub)
                                (i32.const 4)
                                (i32.mul)           ;; i-1 *4
                                (i32.add)           ;; (t-1)*4 + i-1 *4
                                (local.get 0)       ;; x
                                (i32.add)         ;; x + (t-1)*4
                                (i32.load offset=8)     ;; x.c[i-1]
                                (i32.add)         ;; x + (t-1)*4
                                (i32.load offset=8)     ;; x.c[i-1].c[x.c[i-1].n + 1]
                                (i32.store offset=8)    ;; x.c[i].c[0] = x.c[i-1].c[x.c[i-1].n + 1]
                                )
                              )
                            (i32.const 0)
                            (local.set 4) ;; changed = 0
                            )
                          )
                        )
                      )
                    (local.get 4)           ;; changed
                    (i32.const -1)
                    (i32.eq)
                    (if                 ;; changed == -1?
                      (then
                        (local.get 2)       ;; we have to merge x.c[i] with one sibling;; i
                        (i32.const 1)
                        (i32.add)         ;; i+1
                        (local.get 0)       ;; x
                        (i32.load offset=4)     ;; x.n
                        (i32.le_s)
                        (if             ;; i+1 <= x.n
                          (then
                            (local.get 5) ;; merge with right sibling;; x.c[i]
                            (i32.const 4)
                            (local.get 5)
                            (i32.load offset=4)   ;; index = x.c[i].n
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i].keys[x.c[i].n]
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; index = i
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i]
                            (i32.load offset=8)   ;; x.keys[i]
                            (i32.store offset=8)  ;; x.c[i].keys[x.c[i].n] = x.keys[i]
                            (i32.const 0)
                            (local.set 3)   ;; j = 0
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)  ;; j
                                         (i32.const 0)  ;;get x.c[i+1]
                                         (i32.load)           ;; t
                                         (i32.const 1)
                                         (i32.sub)            ;; t-1
                                         (i32.const 4)
                                         (i32.mul)            ;; (t-1)*4
                                         (local.get 2)          ;; i
                                         (i32.const 1)
                                         (i32.add)
                                         (i32.const 4)
                                         (i32.mul)            ;; i+1 *4
                                         (i32.add)            ;; (t-1)*4 + i+1 *4
                                         (local.get 0)        ;; x
                                         (i32.add)          ;; x + (t-1)*4
                                         (i32.load offset=8)      ;; x.c[i+1]
                                         (i32.load offset=4)      ;; x.c[i+1].n
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (local.get 5)      ;; x.c[i]
                                             (i32.const 4)
                                             (local.get 5)
                                             (i32.load offset=4)
                                             (local.get 3)
                                             (i32.add)
                                             (i32.const 1)
                                             (i32.add)        ;; index = x.c[i].n+j+1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.c[i].keys[x.c[i].n+j]
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.const 4)
                                             (local.get 3)        ;; index = j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4
                                             (i32.load offset=8)    ;; x.c[i+1].keys[j]
                                             (i32.store offset=8) ;; x.c[i].keys[x.c[i].n+j] = x.c[i+1].keys[j]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 5)
                            (i32.load)
                            (i32.const 1)
                            (i32.ne)
                            (if
                              (then
                                (i32.const 0)
                                (local.set 3)   ;; j = 0
                                (block $loop_break
                                       (loop $loop
                                             (local.get 3)  ;; j
                                             (i32.const 0)  ;;get x.c[i+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.add)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i+1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i+1]
                                             (i32.load offset=4)      ;; x.c[i+1].n
                                             (i32.const 1)
                                             (i32.add)          ;; x.c[i+1].n + 1
                                             (i32.eq)
                                             (if
                                               (then
                                                 (br $loop_break)
                                                 )
                                               (else
                                                 (i32.const 0)  ;;get x.c[i].c[x.c[i].n+j]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 5)
                                                 (i32.load offset=4)
                                                 (local.get 3)
                                                 (i32.add)
                                                 (i32.const 1)
                                                 (i32.add)            ;; index = x.c[i].n+j+1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i-1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (local.get 5)        ;; x.c[i]
                                                 (i32.add)          ;; x + (t-1)*4  --> addr of x.c[i].c[x.c[i].n+j]
                                                 (i32.const 0)  ;;get x.c[i].c[x.c[i].n+j]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)          ;; index = j
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i-1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (i32.const 0)  ;;get x.c[i+1]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 2)          ;; i
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i+1 *4
                                                 (local.get 0)        ;; x
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i+1]
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i+1].c[j]
                                                 (i32.store offset=8)   ;; x.c[i].c[x.c[i].n+j] = x.c[i+1].c[j]
                                                 (local.get 3)
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (local.set 3)    ;; j = j + 1
                                                 (br $loop)
                                                 )
                                               )
                                             )
                                       )
                                )
                              )
                            (local.get 5)         ;; x.c[i]
                            (local.get 5)         ;; x.c[i]
                            (i32.load offset=4)       ;; x.c[i].n
                            (i32.const 0) ;;get x.c[i+1]
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.add)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i+1 *4
                            (local.get 0)         ;; x
                            (i32.add)           ;; x + (t-1)*4
                            (i32.load offset=8)       ;; x.c[i+1]
                            (i32.load offset=4)       ;; x.c[i+1].n
                            (i32.add)           ;; x.c[i].n + x.c[i+1].n
                            (i32.const 1)
                            (i32.add)           ;; x.c[i].n + x.c[i+1].n + 1
                            (i32.store offset=4)      ;; x.c[i].n = x.c[i].n + x.c[i+1].n + 1
                            (local.get 2)     ;; i
                            (i32.const 1)
                            (i32.add)       ;; i+1
                            (local.set 3)     ;; j = i+1
                            (block $loop_break  ;; re-organize x
                                   (loop $loop
                                         (local.get 3)
                                         (local.get 0)
                                         (i32.load offset=4)  ;; x.n
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (i32.const 0)  ;;get x.c[j]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 3)          ;; index = j
                                             (i32.const 4)
                                             (i32.mul)            ;; i-1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4  --> addr of x.c[j]
                                             (i32.const 0)  ;;get x.c[j+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)            ;; index = j + 1
                                             (i32.const 4)
                                             (i32.mul)            ;; i-1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4  --> addr of x.c[j+1]
                                             (i32.load offset=8)      ;; x.c[j+1]
                                             (i32.store offset=8)   ;; x.c[j] = x.c[j+1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 2)   ;; i
                            (local.set 3)   ;; j = i
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)    ;; j
                                         (local.get 0)
                                         (i32.load offset=4)  ;; x.n
                                         (i32.const 1)
                                         (i32.sub)
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (local.get 0)      ;; x
                                             (i32.const 4)
                                             (local.get 3)      ;; index = j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.keys[j]
                                             (local.get 0)      ;; x
                                             (i32.const 4)
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)        ;; index = j + 1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4
                                             (i32.load offset=8)    ;; x.keys[j+1]
                                             (i32.store offset=8) ;; x.keys[j] = x.keys[j+1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 0)     ;; x
                            (local.get 0)     ;; x
                            (i32.load offset=4)   ;; x.n
                            (i32.const 1)
                            (i32.sub)       ;; x.n - 1
                            (i32.store offset=4)  ;; x.n = x.n - 1
                            (i32.const 0)
                            (local.set 4)     ;; changed = 0
                            )
                          )
                        (local.get 4)   ;; changed
                        (i32.const -1)
                        (i32.eq)      ;; changed == -1 ?
                        (local.get 2)   ;; i
                        (i32.const 1)
                        (i32.sub)     ;; i - 1
                        (i32.const 0)
                        (i32.ge_s)      ;; i - 1 >= 0?
                        (i32.and)     ;; changed == -1 && i - 1 >= 0
                        (if
                          (then
                            (i32.const 0) ;; merge with left sibling    ;;get x.c[i-1]
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.const 4)
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.load offset=4)   ;; index = x.c[i-1].n
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.c[i-1].keys[x.c[i-1].n]
                            (local.get 0)     ;; x
                            (i32.const 4)
                            (local.get 2)     ;; index = i
                            (i32.const 1)
                            (i32.sub)       ;; index = i-1
                            (i32.mul)       ;; i*4
                            (i32.add)       ;; x + i*4 ;; addr of x.keys[i-1]
                            (i32.load offset=8)   ;; x.keys[i]
                            (i32.store offset=8)  ;; x.c[i-1].keys[x.c[i-1].n] = x.keys[i-1]
                            (i32.const 0)
                            (local.set 3)   ;; j = 0
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)  ;; j
                                         (local.get 5)      ;; x.c[i]
                                         (i32.load offset=4)    ;; x.c[i].n
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (i32.const 0)  ;; x.c[i-1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.sub)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i-1]
                                             (i32.const 4)
                                             (i32.const 0)
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 2)          ;; i
                                             (i32.const 1)
                                             (i32.sub)
                                             (i32.const 4)
                                             (i32.mul)            ;; i+1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4
                                             (i32.load offset=8)      ;; x.c[i-1]
                                             (i32.load offset=4)      ;; x.c[i-1].n
                                             (local.get 3)
                                             (i32.add)
                                             (i32.const 1)
                                             (i32.add)        ;; index = x.c[i-1].n+j+1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.c[i-1].keys[x.c[i-1].n+j]
                                             (local.get 5)      ;; x.c[i]
                                             (i32.const 4)
                                             (local.get 3)      ;; index = j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4
                                             (i32.load offset=8)    ;; x.c[i].keys[j]
                                             (i32.store offset=8) ;; x.c[i-1].keys[x.c[i-1].n+j] = x.c[i].keys[j]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 5)
                            (i32.load)
                            (i32.const 1)
                            (i32.ne)
                            (if
                              (then
                                (i32.const 0)
                                (local.set 3)   ;; j = 0
                                (block $loop_break
                                       (loop $loop
                                             (local.get 3)  ;; j
                                             (local.get 5)      ;; x.c[i]
                                             (i32.load offset=4)    ;; x.c[i].n
                                             (i32.const 1)
                                             (i32.add)        ;; x.c[i].n + 1
                                             (i32.eq)
                                             (if
                                               (then
                                                 (br $loop_break)
                                                 )
                                               (else
                                                 (i32.const 0)  ;;get x.c[i-1].c[x.c[i-1].n+j]
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (i32.const 0)
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 2)          ;; i
                                                 (i32.const 1)
                                                 (i32.sub)
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (local.get 0)        ;; x
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i-1]
                                                 (i32.load offset=4)
                                                 (local.get 3)
                                                 (i32.add)
                                                 (i32.const 1)
                                                 (i32.add)            ;; index = x.c[i-1].n+j+1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i-1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (i32.const 0)
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 2)          ;; i
                                                 (i32.const 1)
                                                 (i32.sub)
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i+1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (local.get 0)        ;; x
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i-1]
                                                 (i32.add)          ;; x + (t-1)*4  --> addr of x.c[i-1].c[x.c[i-1].n+j]
                                                 (i32.const 0)
                                                 (i32.load)           ;; t
                                                 (i32.const 1)
                                                 (i32.sub)            ;; t-1
                                                 (i32.const 4)
                                                 (i32.mul)            ;; (t-1)*4
                                                 (local.get 3)          ;; index = j
                                                 (i32.const 4)
                                                 (i32.mul)            ;; i-1 *4
                                                 (i32.add)            ;; (t-1)*4 + i-1 *4
                                                 (local.get 5)        ;; x.c[i]
                                                 (i32.add)          ;; x + (t-1)*4
                                                 (i32.load offset=8)      ;; x.c[i].c[j]
                                                 (i32.store offset=8)   ;; x.c[i-1].c[x.c[i-1].n+j] = x.c[i].c[j]
                                                 (local.get 3)
                                                 (i32.const 1)
                                                 (i32.add)
                                                 (local.set 3)    ;; j = j + 1
                                                 (br $loop)
                                                 )
                                               )
                                             )
                                       )
                                )
                              )
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.const 0)
                            (i32.load)            ;; t
                            (i32.const 1)
                            (i32.sub)           ;; t-1
                            (i32.const 4)
                            (i32.mul)           ;; (t-1)*4
                            (local.get 2)         ;; i
                            (i32.const 1)
                            (i32.sub)
                            (i32.const 4)
                            (i32.mul)           ;; i+1 *4
                            (i32.add)           ;; (t-1)*4 + i-1 *4
                            (local.get 0)       ;; x
                            (i32.add)         ;; x + (t-1)*4
                            (i32.load offset=8)     ;; x.c[i-1]
                            (i32.load offset=4)       ;; x.c[i-1].n
                            (local.get 5)
                            (i32.load offset=4)       ;; x.c[i].n
                            (i32.add)           ;; x.c[i-1].n + x.c[i].n
                            (i32.const 1)
                            (i32.add)           ;; x.c[i].n + x.c[i+1].n + 1
                            (i32.store offset=4)      ;; x.c[i-1].n = x.c[i-1].n + x.c[i].n + 1
                            (local.get 2)     ;; i
                            (local.set 3)     ;; j = i
                            (block $loop_break  ;; re-organize x
                                   (loop $loop
                                         (local.get 3)
                                         (local.get 0)
                                         (i32.load offset=4)  ;; x.n
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (i32.const 0)  ;;get x.c[j]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 3)          ;; index = j
                                             (i32.const 4)
                                             (i32.mul)            ;; i-1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4  --> addr of x.c[j]
                                             (i32.const 0)  ;;get x.c[j+1]
                                             (i32.load)           ;; t
                                             (i32.const 1)
                                             (i32.sub)            ;; t-1
                                             (i32.const 4)
                                             (i32.mul)            ;; (t-1)*4
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)            ;; index = j + 1
                                             (i32.const 4)
                                             (i32.mul)            ;; i-1 *4
                                             (i32.add)            ;; (t-1)*4 + i-1 *4
                                             (local.get 0)        ;; x
                                             (i32.add)          ;; x + (t-1)*4  --> addr of x.c[j]
                                             (i32.load offset=8)      ;; x.c[j+1]
                                             (i32.store offset=8)   ;; x.c[j] = x.c[j+1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 2)   ;; i
                            (i32.const 1)
                            (i32.sub)
                            (local.set 3)   ;; j = i - 1
                            (block $loop_break
                                   (loop $loop
                                         (local.get 3)    ;; j
                                         (local.get 0)
                                         (i32.load offset=4)  ;; x.n
                                         (i32.const 1)
                                         (i32.sub)
                                         (i32.eq)
                                         (if
                                           (then
                                             (br $loop_break)
                                             )
                                           (else
                                             (local.get 0)      ;; x
                                             (i32.const 4)
                                             (local.get 3)      ;; index = j
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4 ;; addr of x.keys[j]
                                             (local.get 0)      ;; x
                                             (i32.const 4)
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)        ;; index = j + 1
                                             (i32.mul)        ;; i*4
                                             (i32.add)        ;; x + i*4
                                             (i32.load offset=8)    ;; x.keys[j+1]
                                             (i32.store offset=8) ;; x.keys[j] = x.keys[j+1]
                                             (local.get 3)
                                             (i32.const 1)
                                             (i32.add)
                                             (local.set 3)    ;; j = j + 1
                                             (br $loop)
                                             )
                                           )
                                         )
                                   )
                            (local.get 0)     ;; x
                            (local.get 0)     ;; x
                            (i32.load offset=4)   ;; x.n
                            (i32.const 1)
                            (i32.sub)       ;; x.n - 1
                            (i32.store offset=4)  ;; x.n = x.n - 1
                            )
                          )
                        )
                      )
                    )
                  )
                (local.get 4)
                (i32.const -1)
                (i32.eq)
                (if     ;; changed == -1? if yes, we merged with left sibling
                  (then
                    (i32.const 0)
                    (i32.load)            ;; t
                    (i32.const 1)
                    (i32.sub)           ;; t-1
                    (i32.const 4)
                    (i32.mul)           ;; (t-1)*4
                    (local.get 2)         ;; i
                    (i32.const 1)
                    (i32.sub)
                    (i32.const 4)
                    (i32.mul)           ;; i+1 *4
                    (i32.add)           ;; (t-1)*4 + i-1 *4
                    (local.get 0)       ;; x
                    (i32.add)         ;; x + (t-1)*4
                    (i32.load offset=8)     ;; x.c[i-1]
                    (local.get 1)
                    (call $btreeDelete)
                    (drop)
                    )
                  (else
                    (local.get 5) ;; x.c[i]
                    (local.get 1)
                    (call $btreeDelete)
                    (drop)
                    )
                  )
                )
              )
            ) ;; end of if x is not leaf
          )
        (i32.const 0)
        (i32.load offset=8)   ;; root addr
        (i32.load offset=4)
        (i32.const 0)
        (i32.eq)
        (if         ;; if root is empty
          (then
            (i32.const 0)
            (i32.const 0)
            (i32.load)            ;; t
            (i32.const 1)
            (i32.sub)           ;; t-1
            (i32.const 4)
            (i32.mul)           ;; (t-1)*4
            (i32.const 0)         ;; index = 0
            (i32.const 4)
            (i32.mul)           ;; i *4
            (i32.add)           ;; (t-1)*4 + i *4
            (i32.const 0)
            (i32.load offset=8)   ;; root addr
            (i32.add)         ;; x + (t-1)*4
            (i32.load offset=8)     ;; root.c[0]
            (i32.store offset=8)  ;; root = root.c[0]
            )
          )
        (i32.const 0)
        (i32.load offset=8)   ;; root addr
        )
  (func $main
        (param $a i32)
        (param $b i32)
        (param $c i32)
        (param $d i32)
        (param $h i32)
        (local $btree i32)
        (local.get $a)
        (local.get $b)
        (i32.gt_s)
        (local.get $b)
        (local.get $c)
        (i32.gt_s)
        (local.get $c)
        (local.get $d)
        (i32.gt_s)
        (local.get $h)
        (local.get $a)
        (i32.ne)
        (local.get $h)
        (local.get $b)
        (i32.ne)
        (local.get $h)
        (local.get $c)
        (i32.ne)
        (local.get $h)
        (local.get $d)
        (i32.ne)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.eqz)
        (if (then (unreachable)))
        (i32.const 4) ;;create a tree with degree 4
        (call $createBtree)
        (local.set $btree)
        ;; insert variables
        (local.get $a)
        (call $btreeInsert)
        (local.set $btree)
        (local.get $b)
        (call $btreeInsert)
        (local.set $btree)
        (local.get $c)
        (call $btreeInsert)
        (local.set $btree)
        (local.get $d)
        (call $btreeInsert)
        (local.set $btree)
        (local.get $h)
        (call $btreeInsert)
        (local.set $btree)
        ;; search for variables & check that they were inserted
        (local.get $btree)
        (local.get $a)
        (call $btreeSearch)
        (i32.const -1)
        (i32.ne)
        (local.get $btree)
        (local.get $b)
        (call $btreeSearch)
        (i32.const -1)
        (i32.ne)
        (local.get $btree)
        (local.get $c)
        (call $btreeSearch)
        (i32.const -1)
        (i32.ne)
        (local.get $btree)
        (local.get $d)
        (call $btreeSearch)
        (i32.const -1)
        (i32.ne)
        (local.get $btree)
        (local.get $h)
        (call $btreeSearch)
        (i32.const -1)
        (i32.ne)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.and)
        ;; delete & check that it was deleted
        ;; a
        (local.get $btree)
        (local.get $a)
        (call $btreeDelete)
        (local.set $btree)
        (local.get $btree)
        (local.get $a)
        (call $btreeSearch)
        (i32.const -1)
        (i32.eq)
        ;; b
        (local.get $btree)
        (local.get $b)
        (call $btreeDelete)
        (local.set $btree)
        (local.get $btree)
        (local.get $b)
        (call $btreeSearch)
        (i32.const -1)
        (i32.eq)
        ;; c
        (local.get $btree)
        (local.get $c)
        (call $btreeDelete)
        (local.set $btree)
        (local.get $btree)
        (local.get $c)
        (call $btreeSearch)
        (i32.const -1)
        (i32.eq)
        ;; d
        (local.get $btree)
        (local.get $d)
        (call $btreeDelete)
        (local.set $btree)
        (local.get $btree)
        (local.get $d)
        (call $btreeSearch)
        (i32.const -1)
        (i32.eq)
        (i32.and)
        (i32.and)
        (i32.and)
        (i32.and)
        (drop))
  (export "main" (func $main)))
