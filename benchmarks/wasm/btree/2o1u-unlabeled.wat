(module
  (type (;0;) (func (param i32) (result i32)))
  (type (;1;) (func (param i32 i32) (result i32)))
  (type (;2;) (func (param i32 i32)))
  (type (;3;) (func (param i32 i32 i32)))
  (type (;4;) (func))
  (func (;0;) (type 0) (param i32) (result i32)
    i32.const 0
    local.get 0
    i32.store
    i32.const 0
    i32.const 1
    i32.store offset=4
    i32.const 0
    i32.const 65536
    i32.store offset=8
    i32.const 1
    memory.grow
    i32.const -1
    i32.ne
    if  ;; label = @1
      i32.const 65536
      i32.const 1
      i32.store
      i32.const 65536
      i32.const 0
      i32.store offset=4
    end
    i32.const 65536)
  (func (;1;) (type 1) (param i32 i32) (result i32)
    (local i32)
    i32.const 0
    local.set 2
    block  ;; label = @1
      loop  ;; label = @2
        local.get 2
        local.get 0
        i32.load offset=4
        i32.const 1
        i32.sub
        i32.le_s
        if  ;; label = @3
          local.get 1
          local.get 0
          i32.const 4
          local.get 2
          i32.mul
          i32.add
          i32.load offset=8
          i32.gt_s
          if  ;; label = @4
            i32.const 1
            local.get 2
            i32.add
            local.set 2
            br 2 (;@2;)
          else
            br 3 (;@1;)
          end
        else
          br 2 (;@1;)
        end
      end
    end
    local.get 2
    local.get 0
    i32.load offset=4
    i32.const 1
    i32.sub
    i32.le_s
    if (result i32)  ;; label = @1
      local.get 1
      local.get 0
      i32.const 4
      local.get 2
      i32.mul
      i32.add
      i32.load offset=8
      i32.eq
      if (result i32)  ;; label = @2
        local.get 0
        i32.const 8
        i32.add
        i32.const 4
        local.get 2
        i32.mul
        i32.add
      else
        local.get 0
        i32.load
        i32.const 1
        i32.eq
        if (result i32)  ;; label = @3
          i32.const -1
        else
          i32.const 0
          i32.load
          i32.const 1
          i32.sub
          i32.const 4
          i32.mul
          local.get 2
          i32.const 4
          i32.mul
          i32.add
          local.get 0
          i32.add
          i32.load offset=8
          local.get 1
          call 1
        end
      end
    else
      local.get 0
      i32.load
      i32.const 1
      i32.eq
      if (result i32)  ;; label = @2
        i32.const -1
      else
        i32.const 0
        i32.load
        i32.const 1
        i32.sub
        i32.const 4
        i32.mul
        local.get 2
        i32.const 4
        i32.mul
        i32.add
        local.get 0
        i32.add
        i32.load offset=8
        local.get 1
        call 1
      end
    end)
  (func (;2;) (type 2) (param i32 i32)
    (local i32 i32)
    i32.const 1
    memory.grow
    i32.const -1
    i32.ne
    if  ;; label = @1
      i32.const 0
      i32.load offset=4
      i32.const 1
      i32.add
      i32.const 65536
      i32.mul
      local.set 2
      i32.const 0
      i32.const 0
      i32.load offset=4
      i32.const 1
      i32.add
      i32.store offset=4
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 1
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      i32.load
      i32.const 1
      i32.eq
      if  ;; label = @2
        local.get 2
        i32.const 1
        i32.store
      else
        local.get 2
        i32.const 0
        i32.store
      end
      local.get 2
      i32.const 0
      i32.load
      i32.const 2
      i32.div_s
      i32.const 1
      i32.sub
      i32.store offset=4
      i32.const 0
      local.set 3
      block  ;; label = @2
        loop  ;; label = @3
          local.get 3
          i32.const 0
          i32.load
          i32.const 2
          i32.div_s
          i32.const 1
          i32.sub
          i32.eq
          if  ;; label = @4
            br 2 (;@2;)
          else
            local.get 2
            i32.const 4
            local.get 3
            i32.mul
            i32.add
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 1
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            i32.const 4
            local.get 3
            i32.const 0
            i32.load
            i32.const 2
            i32.div_s
            i32.add
            i32.mul
            i32.add
            i32.load offset=8
            i32.store offset=8
            local.get 3
            i32.const 1
            i32.add
            local.set 3
            br 1 (;@3;)
          end
        end
      end
      i32.const 0
      local.set 3
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 1
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      i32.load
      i32.const 1
      i32.ne
      if  ;; label = @2
        block  ;; label = @3
          loop  ;; label = @4
            local.get 3
            i32.const 0
            i32.load
            i32.const 2
            i32.div_s
            i32.eq
            if  ;; label = @5
              br 2 (;@3;)
            else
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 3
              i32.const 4
              i32.mul
              i32.add
              local.get 2
              i32.add
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 1
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 3
              i32.const 0
              i32.load
              i32.const 2
              i32.div_s
              i32.add
              i32.const 4
              i32.mul
              i32.add
              i32.add
              i32.load offset=8
              i32.store offset=8
              local.get 3
              i32.const 1
              i32.add
              local.set 3
              br 1 (;@4;)
            end
          end
        end
      end
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 1
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      i32.const 0
      i32.load
      i32.const 2
      i32.div_s
      i32.const 1
      i32.sub
      i32.store offset=4
      local.get 0
      i32.load offset=4
      local.set 3
      block  ;; label = @2
        loop  ;; label = @3
          local.get 1
          local.get 3
          i32.eq
          if  ;; label = @4
            br 2 (;@2;)
          else
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 3
            i32.const 1
            i32.add
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 3
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            i32.store offset=8
            local.get 3
            i32.const 1
            i32.sub
            local.set 3
            br 1 (;@3;)
          end
        end
      end
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 1
      i32.const 1
      i32.add
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      local.get 2
      i32.store offset=8
      local.get 0
      i32.load offset=4
      i32.const 1
      i32.sub
      local.set 3
      block  ;; label = @2
        loop  ;; label = @3
          local.get 1
          i32.const 1
          i32.sub
          local.get 3
          i32.eq
          if  ;; label = @4
            br 2 (;@2;)
          else
            local.get 3
            i32.const 1
            i32.add
            i32.const 4
            i32.mul
            local.get 0
            i32.add
            local.get 3
            i32.const 4
            i32.mul
            local.get 0
            i32.add
            i32.load offset=8
            i32.store offset=8
            local.get 3
            i32.const 1
            i32.sub
            local.set 3
            br 1 (;@3;)
          end
        end
      end
      local.get 1
      i32.const 4
      i32.mul
      local.get 0
      i32.add
      i32.const 0
      i32.load
      i32.const 2
      i32.div_s
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 1
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      i32.add
      i32.load offset=8
      i32.store offset=8
      local.get 0
      local.get 0
      i32.load offset=4
      i32.const 1
      i32.add
      i32.store offset=4
    end)
  (func (;3;) (type 2) (param i32 i32)
    (local i32 i32)
    local.get 0
    i32.load offset=4
    i32.const 1
    i32.sub
    local.set 2
    local.get 0
    i32.load
    i32.const 1
    i32.eq
    if  ;; label = @1
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.const 0
          i32.ge_s
          if (result i32)  ;; label = @4
            local.get 1
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            i32.load offset=8
            i32.lt_s
          else
            i32.const 0
          end
          local.get 2
          i32.const 0
          i32.ge_s
          i32.and
          if  ;; label = @4
            local.get 0
            i32.const 4
            local.get 2
            i32.const 1
            i32.add
            i32.mul
            i32.add
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            i32.load offset=8
            i32.store offset=8
            local.get 2
            i32.const 1
            i32.sub
            local.set 2
            br 1 (;@3;)
          else
            br 2 (;@2;)
          end
        end
      end
      local.get 0
      i32.const 4
      local.get 2
      i32.const 1
      i32.add
      i32.mul
      i32.add
      local.get 1
      i32.store offset=8
      local.get 0
      local.get 0
      i32.load offset=4
      i32.const 1
      i32.add
      i32.store offset=4
    else
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          i32.const 0
          i32.ge_s
          if  ;; label = @4
            local.get 1
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            i32.load offset=8
            i32.lt_s
            if  ;; label = @5
              local.get 2
              i32.const 1
              i32.sub
              local.set 2
              br 2 (;@3;)
            else
              br 3 (;@2;)
            end
          else
            br 2 (;@2;)
          end
        end
      end
      local.get 2
      i32.const 1
      i32.add
      local.set 2
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 2
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      i32.load offset=4
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.eq
      if  ;; label = @2
        local.get 0
        local.get 2
        call 2
        local.get 1
        local.get 0
        i32.const 4
        local.get 2
        i32.mul
        i32.add
        i32.load offset=8
        i32.gt_s
        if  ;; label = @3
          local.get 2
          i32.const 1
          i32.add
          local.set 2
        end
      end
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      local.get 2
      i32.const 4
      i32.mul
      i32.add
      local.get 0
      i32.add
      i32.load offset=8
      local.get 1
      call 3
    end)
  (func (;4;) (type 0) (param i32) (result i32)
    (local i32 i32)
    i32.const 0
    i32.load offset=8
    local.tee 2
    i32.load offset=4
    i32.const 0
    i32.load
    i32.const 1
    i32.sub
    i32.eq
    if (result i32)  ;; label = @1
      i32.const 1
      memory.grow
      i32.const -1
      i32.ne
      if (result i32)  ;; label = @2
        i32.const 0
        i32.load offset=4
        i32.const 1
        i32.add
        i32.const 65536
        i32.mul
        local.set 1
        i32.const 0
        i32.const 0
        i32.load offset=4
        i32.const 1
        i32.add
        i32.store offset=4
        i32.const 0
        local.get 1
        i32.store offset=8
        local.get 1
        i32.const 0
        i32.store
        local.get 1
        i32.const 0
        i32.store offset=4
        i32.const 0
        i32.load
        i32.const 1
        i32.sub
        i32.const 4
        i32.mul
        i32.const 0
        i32.const 4
        i32.mul
        i32.add
        local.get 1
        i32.add
        local.get 2
        i32.store offset=8
        local.get 1
        i32.const 0
        call 2
        local.get 1
        local.get 0
        call 3
        local.get 1
      else
        i32.const -1
      end
    else
      local.get 2
      local.get 0
      call 3
      local.get 2
    end)
  (func (;5;) (type 1) (param i32 i32) (result i32)
    (local i32 i32 i32 i32)
    local.get 0
    i32.load
    i32.const 1
    i32.eq
    if  ;; label = @1
      i32.const 0
      local.set 2
      block  ;; label = @2
        loop  ;; label = @3
          local.get 0
          i32.load offset=4
          local.get 2
          i32.eq
          if  ;; label = @4
            br 2 (;@2;)
          else
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            i32.load offset=8
            local.get 1
            i32.eq
            if  ;; label = @5
              local.get 2
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 0
                  i32.load offset=4
                  i32.const 1
                  i32.sub
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 0
              local.get 0
              i32.load offset=4
              i32.const 1
              i32.sub
              i32.store offset=4
              br 3 (;@2;)
            end
          end
          i32.const 1
          local.get 2
          i32.add
          local.set 2
          br 0 (;@3;)
        end
      end
    else
      i32.const 0
      local.set 2
      block  ;; label = @2
        loop  ;; label = @3
          local.get 2
          local.get 0
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.le_s
          if (result i32)  ;; label = @4
            local.get 1
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            i32.load offset=8
            i32.gt_s
          else
            i32.const 0
          end
          local.get 2
          local.get 0
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.le_s
          i32.and
          if  ;; label = @4
            local.get 2
            i32.const 1
            i32.add
            local.set 2
            br 1 (;@3;)
          else
            br 2 (;@2;)
          end
        end
      end
      local.get 2
      local.get 0
      i32.load offset=4
      i32.lt_s
      if (result i32)  ;; label = @2
        local.get 0
        i32.const 4
        local.get 2
        i32.mul
        i32.add
        i32.load offset=8
        local.get 1
        i32.eq
      else
        i32.const 0
      end
      if  ;; label = @2
        i32.const 0
        i32.load
        i32.const 1
        i32.sub
        i32.const 4
        i32.mul
        local.get 2
        i32.const 4
        i32.mul
        i32.add
        local.get 0
        i32.add
        i32.load offset=8
        local.tee 5
        i32.load offset=4
        i32.const 0
        i32.load
        i32.const 2
        i32.div_s
        i32.ge_s
        if  ;; label = @3
          local.get 0
          i32.const 4
          local.get 2
          i32.mul
          i32.add
          local.get 5
          i32.const 4
          local.get 5
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.mul
          i32.add
          i32.load offset=8
          local.get 5
          local.get 5
          i32.const 4
          local.get 5
          i32.load offset=4
          i32.const 1
          i32.sub
          i32.mul
          i32.add
          i32.load offset=8
          call 5
          drop
          i32.store offset=8
        else
          i32.const 0
          i32.load
          i32.const 1
          i32.sub
          i32.const 4
          i32.mul
          local.get 2
          i32.const 1
          i32.add
          i32.const 4
          i32.mul
          i32.add
          local.get 0
          i32.add
          i32.load offset=8
          local.tee 5
          i32.load offset=4
          i32.const 0
          i32.load
          i32.const 2
          i32.div_s
          i32.ge_s
          if  ;; label = @4
            local.get 0
            i32.const 4
            local.get 2
            i32.mul
            i32.add
            local.get 5
            i32.const 4
            i32.const 0
            i32.mul
            i32.add
            i32.load offset=8
            local.get 5
            local.get 5
            i32.const 4
            i32.const 0
            i32.mul
            i32.add
            i32.load offset=8
            call 5
            drop
            i32.store offset=8
          else
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 2
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            local.set 5
            local.get 5
            i32.const 4
            local.get 5
            i32.load offset=4
            i32.mul
            i32.add
            local.get 1
            i32.store offset=8
            i32.const 0
            local.set 3
            block  ;; label = @5
              loop  ;; label = @6
                local.get 3
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                local.get 2
                i32.const 1
                i32.add
                i32.const 4
                i32.mul
                i32.add
                local.get 0
                i32.add
                i32.load offset=8
                i32.load offset=4
                i32.eq
                if  ;; label = @7
                  br 2 (;@5;)
                else
                  local.get 5
                  i32.const 4
                  local.get 5
                  i32.load offset=4
                  local.get 3
                  i32.add
                  i32.const 1
                  i32.add
                  i32.mul
                  i32.add
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 2
                  i32.const 1
                  i32.add
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.load offset=8
                  i32.const 4
                  local.get 3
                  i32.mul
                  i32.add
                  i32.load offset=8
                  i32.store offset=8
                  local.get 3
                  i32.const 1
                  i32.add
                  local.set 3
                  br 1 (;@6;)
                end
              end
            end
            local.get 5
            local.get 5
            i32.load offset=4
            i32.const 1
            i32.add
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 2
            i32.const 1
            i32.add
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            i32.load offset=4
            i32.add
            i32.store offset=4
            local.get 5
            i32.load
            i32.const 1
            i32.ne
            if  ;; label = @5
              i32.const 0
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 2
                  i32.const 1
                  i32.add
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.load offset=8
                  i32.load offset=4
                  i32.const 1
                  i32.add
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 5
                    i32.load offset=4
                    local.get 3
                    i32.add
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 5
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 3
                    i32.const 4
                    i32.mul
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
            end
            local.get 2
            local.set 3
            block  ;; label = @5
              loop  ;; label = @6
                local.get 0
                i32.load offset=4
                i32.const 1
                i32.sub
                local.get 3
                i32.eq
                if  ;; label = @7
                  br 2 (;@5;)
                else
                  local.get 0
                  i32.const 4
                  local.get 3
                  i32.mul
                  i32.add
                  local.get 0
                  i32.const 4
                  local.get 3
                  i32.const 1
                  i32.add
                  i32.mul
                  i32.add
                  i32.load offset=8
                  i32.store offset=8
                  local.get 3
                  i32.const 1
                  i32.add
                  local.set 3
                  br 1 (;@6;)
                end
              end
            end
            local.get 2
            i32.const 1
            i32.add
            local.set 3
            block  ;; label = @5
              loop  ;; label = @6
                local.get 0
                i32.load offset=4
                local.get 3
                i32.eq
                if  ;; label = @7
                  br 2 (;@5;)
                else
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 3
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 3
                  i32.const 1
                  i32.add
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.load offset=8
                  i32.store offset=8
                  local.get 3
                  i32.const 1
                  i32.add
                  local.set 3
                  br 1 (;@6;)
                end
              end
            end
            local.get 0
            local.get 0
            i32.load offset=4
            i32.const 1
            i32.sub
            i32.store offset=4
            local.get 5
            local.get 1
            call 5
            drop
          end
        end
      else
        i32.const 0
        i32.load
        i32.const 1
        i32.sub
        i32.const 4
        i32.mul
        local.get 2
        i32.const 4
        i32.mul
        i32.add
        local.get 0
        i32.add
        i32.load offset=8
        local.set 5
        local.get 5
        i32.load offset=4
        i32.const 0
        i32.load
        i32.const 2
        i32.div_s
        i32.const 1
        i32.sub
        i32.eq
        if  ;; label = @3
          i32.const -1
          local.set 4
          local.get 2
          i32.const 1
          i32.add
          local.get 0
          i32.load offset=4
          i32.le_s
          if  ;; label = @4
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 2
            i32.const 1
            i32.add
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            i32.load offset=4
            i32.const 0
            i32.load
            i32.const 2
            i32.div_s
            i32.ge_s
            if  ;; label = @5
              local.get 5
              i32.const 4
              i32.const 0
              i32.load
              i32.const 2
              i32.div_s
              i32.const 1
              i32.sub
              i32.mul
              i32.add
              local.get 0
              i32.const 4
              local.get 2
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              local.get 5
              local.get 5
              i32.load offset=4
              i32.const 1
              i32.add
              i32.store offset=4
              local.get 0
              i32.const 4
              local.get 2
              i32.mul
              i32.add
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.add
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 4
              i32.const 0
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.const 0
                i32.load
                i32.const 2
                i32.div_s
                i32.const 4
                i32.mul
                i32.add
                local.get 5
                i32.add
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.const 0
                i32.const 4
                i32.mul
                i32.add
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                local.get 2
                i32.const 1
                i32.add
                i32.const 4
                i32.mul
                i32.add
                local.get 0
                i32.add
                i32.load offset=8
                i32.add
                i32.load offset=8
                i32.store offset=8
              end
              local.get 5
              i32.const 0
              i32.load
              i32.const 2
              i32.div_s
              i32.store offset=4
              i32.const 0
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 2
                  i32.const 1
                  i32.add
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.load offset=8
                  i32.load offset=4
                  i32.const 1
                  i32.sub
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.const 4
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                i32.const 0
                local.set 3
                block  ;; label = @7
                  loop  ;; label = @8
                    local.get 3
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.load offset=4
                    i32.eq
                    if  ;; label = @9
                      br 2 (;@7;)
                    else
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 4
                      i32.mul
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 2
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 0
                      i32.add
                      i32.load offset=8
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 2
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 0
                      i32.add
                      i32.load offset=8
                      i32.add
                      i32.load offset=8
                      i32.store offset=8
                      local.get 3
                      i32.const 1
                      i32.add
                      local.set 3
                      br 1 (;@8;)
                    end
                  end
                end
              end
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.add
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.add
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              i32.const 1
              i32.sub
              i32.store offset=4
              i32.const 0
              local.set 4
            end
          end
          local.get 4
          i32.const -1
          i32.eq
          local.get 2
          i32.const 1
          i32.sub
          i32.const 0
          i32.ge_s
          i32.and
          if  ;; label = @4
            i32.const 0
            i32.load
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            local.get 2
            i32.const 1
            i32.sub
            i32.const 4
            i32.mul
            i32.add
            local.get 0
            i32.add
            i32.load offset=8
            i32.load offset=4
            i32.const 0
            i32.load
            i32.const 2
            i32.div_s
            i32.ge_s
            if  ;; label = @5
              local.get 5
              i32.load offset=4
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  i32.const 0
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    local.get 5
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    local.get 5
                    i32.const 4
                    local.get 3
                    i32.const 1
                    i32.sub
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.sub
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                local.get 5
                i32.load offset=4
                i32.const 1
                i32.add
                local.set 3
                block  ;; label = @7
                  loop  ;; label = @8
                    local.get 3
                    i32.const 0
                    i32.eq
                    if  ;; label = @9
                      br 2 (;@7;)
                    else
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 5
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 5
                      i32.add
                      i32.load offset=8
                      i32.store offset=8
                      local.get 3
                      i32.const 1
                      i32.sub
                      local.set 3
                      br 1 (;@8;)
                    end
                  end
                end
              end
              local.get 5
              local.get 5
              i32.load offset=4
              i32.const 1
              i32.add
              i32.store offset=4
              local.get 5
              i32.const 4
              i32.const 0
              i32.mul
              i32.add
              local.get 0
              i32.const 4
              local.get 2
              i32.const 1
              i32.sub
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              i32.const 1
              i32.sub
              i32.store offset=4
              local.get 0
              i32.const 4
              local.get 2
              i32.const 1
              i32.sub
              i32.mul
              i32.add
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 4
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.const 0
                i32.const 4
                i32.mul
                i32.add
                local.get 5
                i32.add
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                local.get 2
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.add
                local.get 0
                i32.add
                i32.load offset=8
                i32.load offset=4
                i32.const 1
                i32.add
                i32.const 4
                i32.mul
                i32.add
                i32.const 0
                i32.load
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                local.get 2
                i32.const 1
                i32.sub
                i32.const 4
                i32.mul
                i32.add
                local.get 0
                i32.add
                i32.load offset=8
                i32.add
                i32.load offset=8
                i32.store offset=8
              end
              i32.const 0
              local.set 4
            end
          end
          local.get 4
          i32.const -1
          i32.eq
          if  ;; label = @4
            local.get 2
            i32.const 1
            i32.add
            local.get 0
            i32.load offset=4
            i32.le_s
            if  ;; label = @5
              local.get 5
              i32.const 4
              local.get 5
              i32.load offset=4
              i32.mul
              i32.add
              local.get 0
              i32.const 4
              local.get 2
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              i32.const 0
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  i32.const 0
                  i32.load
                  i32.const 1
                  i32.sub
                  i32.const 4
                  i32.mul
                  local.get 2
                  i32.const 1
                  i32.add
                  i32.const 4
                  i32.mul
                  i32.add
                  local.get 0
                  i32.add
                  i32.load offset=8
                  i32.load offset=4
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    local.get 5
                    i32.const 4
                    local.get 5
                    i32.load offset=4
                    local.get 3
                    i32.add
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                i32.const 0
                local.set 3
                block  ;; label = @7
                  loop  ;; label = @8
                    local.get 3
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.load offset=4
                    i32.const 1
                    i32.add
                    i32.eq
                    if  ;; label = @9
                      br 2 (;@7;)
                    else
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 5
                      i32.load offset=4
                      local.get 3
                      i32.add
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 5
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 4
                      i32.mul
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 2
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 0
                      i32.add
                      i32.load offset=8
                      i32.add
                      i32.load offset=8
                      i32.store offset=8
                      local.get 3
                      i32.const 1
                      i32.add
                      local.set 3
                      br 1 (;@8;)
                    end
                  end
                end
              end
              local.get 5
              local.get 5
              i32.load offset=4
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.add
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              i32.add
              i32.const 1
              i32.add
              i32.store offset=4
              local.get 2
              i32.const 1
              i32.add
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 0
                  i32.load offset=4
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 3
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 2
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 0
                  i32.load offset=4
                  i32.const 1
                  i32.sub
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 0
              local.get 0
              i32.load offset=4
              i32.const 1
              i32.sub
              i32.store offset=4
              i32.const 0
              local.set 4
            end
            local.get 4
            i32.const -1
            i32.eq
            local.get 2
            i32.const 1
            i32.sub
            i32.const 0
            i32.ge_s
            i32.and
            if  ;; label = @5
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 4
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              i32.mul
              i32.add
              local.get 0
              i32.const 4
              local.get 2
              i32.const 1
              i32.sub
              i32.mul
              i32.add
              i32.load offset=8
              i32.store offset=8
              i32.const 0
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 5
                  i32.load offset=4
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.const 4
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 2
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.load offset=4
                    local.get 3
                    i32.add
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    local.get 5
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 5
              i32.load
              i32.const 1
              i32.ne
              if  ;; label = @6
                i32.const 0
                local.set 3
                block  ;; label = @7
                  loop  ;; label = @8
                    local.get 3
                    local.get 5
                    i32.load offset=4
                    i32.const 1
                    i32.add
                    i32.eq
                    if  ;; label = @9
                      br 2 (;@7;)
                    else
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 2
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 0
                      i32.add
                      i32.load offset=8
                      i32.load offset=4
                      local.get 3
                      i32.add
                      i32.const 1
                      i32.add
                      i32.const 4
                      i32.mul
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 2
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 0
                      i32.add
                      i32.load offset=8
                      i32.add
                      i32.const 0
                      i32.load
                      i32.const 1
                      i32.sub
                      i32.const 4
                      i32.mul
                      local.get 3
                      i32.const 4
                      i32.mul
                      i32.add
                      local.get 5
                      i32.add
                      i32.load offset=8
                      i32.store offset=8
                      local.get 3
                      i32.const 1
                      i32.add
                      local.set 3
                      br 1 (;@8;)
                    end
                  end
                end
              end
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.const 0
              i32.load
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              local.get 2
              i32.const 1
              i32.sub
              i32.const 4
              i32.mul
              i32.add
              local.get 0
              i32.add
              i32.load offset=8
              i32.load offset=4
              local.get 5
              i32.load offset=4
              i32.add
              i32.const 1
              i32.add
              i32.store offset=4
              local.get 2
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 0
                  i32.load offset=4
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 3
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.const 0
                    i32.load
                    i32.const 1
                    i32.sub
                    i32.const 4
                    i32.mul
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.const 4
                    i32.mul
                    i32.add
                    local.get 0
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 2
              i32.const 1
              i32.sub
              local.set 3
              block  ;; label = @6
                loop  ;; label = @7
                  local.get 3
                  local.get 0
                  i32.load offset=4
                  i32.const 1
                  i32.sub
                  i32.eq
                  if  ;; label = @8
                    br 2 (;@6;)
                  else
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.mul
                    i32.add
                    local.get 0
                    i32.const 4
                    local.get 3
                    i32.const 1
                    i32.add
                    i32.mul
                    i32.add
                    i32.load offset=8
                    i32.store offset=8
                    local.get 3
                    i32.const 1
                    i32.add
                    local.set 3
                    br 1 (;@7;)
                  end
                end
              end
              local.get 0
              local.get 0
              i32.load offset=4
              i32.const 1
              i32.sub
              i32.store offset=4
            end
          end
        end
        local.get 4
        i32.const -1
        i32.eq
        if  ;; label = @3
          i32.const 0
          i32.load
          i32.const 1
          i32.sub
          i32.const 4
          i32.mul
          local.get 2
          i32.const 1
          i32.sub
          i32.const 4
          i32.mul
          i32.add
          local.get 0
          i32.add
          i32.load offset=8
          local.get 1
          call 5
          drop
        else
          local.get 5
          local.get 1
          call 5
          drop
        end
      end
    end
    i32.const 0
    i32.load offset=8
    i32.load offset=4
    i32.const 0
    i32.eq
    if  ;; label = @1
      i32.const 0
      i32.const 0
      i32.load
      i32.const 1
      i32.sub
      i32.const 4
      i32.mul
      i32.const 0
      i32.const 4
      i32.mul
      i32.add
      i32.const 0
      i32.load offset=8
      i32.add
      i32.load offset=8
      i32.store offset=8
    end
    i32.const 0
    i32.load offset=8)
  (func (;6;) (type 3) (param i32 i32 i32)
    (local i32)
    local.get 0
    local.get 1
    i32.gt_s
    local.get 0
    local.get 2
    i32.ne
    local.get 1
    local.get 2
    i32.ne
    i32.and
    i32.and
    i32.eqz
    if  ;; label = @1
      unreachable
    end
    i32.const 4
    call 0
    local.set 3
    local.get 0
    call 4
    local.set 3
    local.get 1
    call 4
    local.set 3
    local.get 2
    call 4
    local.set 3
    local.get 3
    local.get 0
    call 1
    i32.const -1
    i32.ne
    local.get 3
    local.get 1
    call 1
    i32.const -1
    i32.ne
    local.get 3
    local.get 2
    call 1
    i32.const -1
    i32.ne
    i32.and
    i32.and
    local.get 3
    local.get 0
    call 5
    local.tee 3
    local.get 0
    call 1
    i32.const -1
    i32.eq
    local.get 3
    local.get 1
    call 5
    local.tee 3
    local.get 1
    call 1
    i32.const -1
    i32.eq
    i32.and
    i32.and
    drop)
  (func (;7;) (type 4)
    i32.const 3
    i32.const 2
    i32.const 1
    call 6)
  (memory (;0;) 2)
  (export "main" (func 7))
  (start 7))
